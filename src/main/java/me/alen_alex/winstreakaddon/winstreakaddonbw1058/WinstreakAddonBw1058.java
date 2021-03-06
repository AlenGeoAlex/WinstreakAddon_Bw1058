package me.alen_alex.winstreakaddon.winstreakaddonbw1058;

import co.aikar.commands.BukkitCommandManager;
import com.andrei1058.bedwars.api.BedWars;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.command.Command;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.command.subcommand.SetCurrentCommand;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.SQL;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.SQLite;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.filesystem.Configuration;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.DataStorage;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener.BedwarsListener;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener.PlayerJoinEvents;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener.PlayerLeaveEvent;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager.WinstreakManager;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.task.SaveDataTask;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.FileUtils;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.MessageUtils;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.PermissionData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WinstreakAddonBw1058 extends JavaPlugin {

    private static WinstreakAddonBw1058 plugin;
    private FileUtils fileUtils;
    private Configuration pluginConfig;
    private DataStorage dataStorage;
    private BedWars bedWarsAPI;
    private MessageUtils messageUtils;
    private WinstreakManager streakManager;
    private SaveDataTask playerSaving;
    private BukkitCommandManager commandManager;
    private boolean forcedSQLite = false,bedwarsEnabled = false;
    @Override
    public void onEnable() {
        plugin = this;
        /*
        Checking for dependency plugins, if not present, plugin will be disabled!
        -> PlaceholderAPI
        -> Bedwars1058
         */
        if(!getServer().getPluginManager().isPluginEnabled("BedWars1058")){
            getLogger().severe("-----------------------------------------------");
            getLogger().info(" ");
            getLogger().severe("Dependency plugin missing: BedWars1058");
            getLogger().severe("Plugin will be disabled!");
            getLogger().info(" ");
            getLogger().severe("-----------------------------------------------");
            getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            getLogger().severe("-----------------------------------------------");
            getLogger().info(" ");
            getLogger().severe("Dependency plugin missing: PlaceholderAPI");
            getLogger().severe("Plugin will be disabled!");
            getLogger().info(" ");
            getLogger().severe("-----------------------------------------------");
            getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        /*
        Getting the API class of BedWars1058, If failed plugin will be disabled!
         */
        bedWarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        if(bedWarsAPI == null){
            getLogger().severe("-----------------------------------------------");
            getLogger().info(" ");
            getLogger().severe("Unable to initialize the BedWars1058 plugin API");
            getLogger().severe("Plugin will be disabled!");
            getLogger().info(" ");
            getLogger().severe("-----------------------------------------------");
            getServer().getPluginManager().disablePlugin(plugin);
            return;
        }else bedwarsEnabled = true;
        /*
        Intializing the plugins basic Utils and Configuration Classes
         */
        messageUtils = new MessageUtils(this);
        fileUtils = new FileUtils(this);
        pluginConfig = new Configuration(this);
        pluginConfig.loadFile();
        playerSaving = new SaveDataTask(this);

        /*
        Setting up the datastorage method for plugin, If mySQL is enabled in the config, it will prepare for the server connection, If failed then it will check for fallback SQLite Connection,
        If that too failed, the plugin will be disabled since it cannot implement a storage system.

        If SQLite is enabled, It will prepare for SQLite and try to prepare a connection to SQLite file, If failed, the plugin will be disabled!
         */
        if(pluginConfig.isSql()) {
            dataStorage = new SQL(this);
            if(!dataStorage.init()){
                getLogger().warning("Failed to initialize SQL, Trying SQLite!!");
                dataStorage = null;
                dataStorage = new SQLite(this);
                if(!dataStorage.init()){
                    getLogger().severe("-----------------------------------------------");
                    getLogger().info(" ");
                    getLogger().severe("Unable to initialize both SQL and SQLite");
                    getLogger().severe("Plugin will be disabled!");
                    getLogger().info(" ");
                    getLogger().severe("-----------------------------------------------");
                    getServer().getPluginManager().disablePlugin(plugin);
                    return;
                }else
                    forcedSQLite = true;

            }
        }
        else {
            dataStorage = new SQLite(this);
            if(!dataStorage.init()){
                getLogger().severe("-----------------------------------------------");
                getLogger().info(" ");
                getLogger().severe("Unable to initialize SQLite");
                getLogger().severe("Plugin will be disabled!");
                getLogger().info(" ");
                getLogger().severe("-----------------------------------------------");
                getServer().getPluginManager().disablePlugin(plugin);
                return;
            }
        }

        streakManager = new WinstreakManager(this);
        if(pluginConfig.isSavePlayerData())
            playerSaving.runTaskTimerAsynchronously(this, 20L, getPluginConfig().getSaveDuration() *20*60);
        registerListeners();
        registerCommands();
        /*commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new WSAdmin(this));*/
        this.getLogger().info("Win-streak addon has been properly enabled!!");
        this.getLogger().info("https://github.com/AlenGeoAlex/WinstreakAddon_Bw1058");
    }

    @Override
    public void onDisable() {
        if(dataStorage != null){
            dataStorage.closeConnection();
            if(dataStorage instanceof SQL)
                getLogger().info("MySQL Storage Connection has been closed!");
            else
                getLogger().info("SQLite Storage Connection has been closed!");
        }
    }

    public void reloadPlugin(){
        playerSaving.cancel();
        pluginConfig.reloadFile();
        if(pluginConfig.isSavePlayerData())
            playerSaving.runTaskTimerAsynchronously(this, 20L, getPluginConfig().getSaveDuration() *20*60);
        this.getLogger().info("Plugin has been reloaded!");
    }

    private void registerListeners(){
        this.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(this),this);
        if(bedwarsEnabled)
            this.getServer().getPluginManager().registerEvents(new BedwarsListener(this), this);
    }

    public void registerCommands(){
        Command adminCommand = new Command(this,"wsadmin", PermissionData.ADMIN_PERMISSION.getPermission());
        adminCommand.register();
        adminCommand.registerSubCommand("setcurrent",new SetCurrentCommand());
    }


    public static WinstreakAddonBw1058 getPlugin() {
        return plugin;
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }

    public MessageUtils getMessageUtils() {
        return messageUtils;
    }

    public Configuration getPluginConfig() {
        return pluginConfig;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public BedWars getBedWarsAPI() {
        return bedWarsAPI;
    }

    public WinstreakManager getStreakManager() {
        return streakManager;
    }

    public boolean isForcedSQLite() {
        return forcedSQLite;
    }

    public boolean isBedwarsEnabled() {
        return bedwarsEnabled;
    }
}