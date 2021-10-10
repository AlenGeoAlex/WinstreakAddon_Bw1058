package me.alen_alex.winstreakaddon.winstreakaddonbw1058;

import com.andrei1058.bedwars.api.BedWars;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.SQL;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.SQLite;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.filesystem.Configuration;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.DataStorage;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener.PlayerJoinEvents;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager.WinstreakManager;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.FileUtils;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.MessageUtils;
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

    @Override
    public void onEnable() {
        plugin = this;
        if(!getServer().getPluginManager().isPluginEnabled("BedWars1058")){
            getLogger().severe("-----------------------------------------------");
            getLogger().info(" ");
            getLogger().severe("Dependency plugin missing: Bedwars1058");
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

        bedWarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        messageUtils = new MessageUtils(this);
        fileUtils = new FileUtils(this);
        pluginConfig = new Configuration(this);
        pluginConfig.loadFile();

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
                }
            }
        }
        else
            dataStorage = new SQLite(this);

        streakManager = new WinstreakManager();
        registerListeners();
        this.getLogger().info("Winstreak addon has been properly enabled!!");
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
        pluginConfig.reloadFile();
        this.getLogger().info("Plugin has been reloaded!");
    }

    private void registerListeners(){
        this.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(this), this);
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
}