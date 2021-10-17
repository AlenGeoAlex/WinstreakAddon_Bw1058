package me.alen_alex.winstreakaddon.winstreakaddonbw1058.filesystem;

import de.leonhard.storage.Config;
import de.leonhard.storage.Yaml;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.YamlFiles;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;

public class Configuration implements YamlFiles {

    private WinstreakAddonBw1058 plugin;
    private Config pluginConfiguration;
    private Messages messages;
    private String version;
    //
    private String sqlHost,sqlPort,sqlUsername,sqlPassword,sqlDatabase;
    private boolean sql,doKickonFail,savePlayerData,broadcastPlayerNewStreak,broadcastTouchableRespect,playSoundOnBroadcast;
    private Sound broadcastSound;
    private String kickMessage;
    private int saveDuration;

    public Configuration(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        messages = new Messages(plugin);
        init();
    }

    @Override
    public void init() {
        pluginConfiguration = plugin.getFileUtils().createConfiguration();
        version = pluginConfiguration.getOrSetDefault("version",plugin.getDescription().getVersion());
        plugin.getLogger().info("Configuration has been intiated with version "+version);
        messages.init();
    }

    @Override
    public void loadFile() {
        sql = pluginConfiguration.getBoolean("storage.use-mysql");
        sqlHost = pluginConfiguration.getString("storage.host");
        sqlUsername = pluginConfiguration.getString("storage.username");
        sqlPassword = pluginConfiguration.getString("storage.password");
        sqlPort = pluginConfiguration.getString("storage.port");
        sqlDatabase = pluginConfiguration.getString("storage.database");
        if(StringUtils.isBlank(sqlHost) || StringUtils.isBlank(sqlDatabase) || StringUtils.isBlank(sqlUsername) || StringUtils.isBlank(sqlPort)){
            if(sql)
                plugin.getLogger().warning("Configuration related to SQL are missing, Falling back to SQLite!");
            sql = false;
        }
        doKickonFail = pluginConfiguration.getBoolean("settings.kick-when-failed-to-register-a-player");
        kickMessage = plugin.getMessageUtils().parseColor(pluginConfiguration.getString("settings.kick-on-failed-message"));
        savePlayerData = pluginConfiguration.getBoolean("settings.save-data.enabled");
        saveDuration = pluginConfiguration.getInt("settings.save-data.interval-in-mins");
        broadcastPlayerNewStreak = pluginConfiguration.getBoolean("settings.broadcast-players-new-best-streak.enabled");
        broadcastTouchableRespect = pluginConfiguration.getBoolean("settings.broadcast-players-new-best-streak.touchable-pay-respect");
        playSoundOnBroadcast = pluginConfiguration.getBoolean("settings.broadcast-players-new-best-streak.play-sound.enabled");
        if(EnumUtils.isValidEnum(Sound.class,pluginConfiguration.getString("settings.broadcast-players-new-best-streak.play-sound.broadcast-sound")))
            broadcastSound = Sound.valueOf(pluginConfiguration.getString("settings.broadcast-players-new-best-streak.play-sound.broadcast-sound"));
        else {
            plugin.getLogger().warning("The broadcast sound supplied does not match for the server version! Using default sound NOTE_PLING");
            broadcastSound = Sound.NOTE_PLING;
        }
    }

    @Override
    public Yaml getFile() {
        return (Yaml) pluginConfiguration;
    }

    @Override
    public void reloadFile() {
        init();
        loadFile();
    }

    @Override
    public String getFileVersion() {
        return version;
    }

    public String getSqlHost() {
        return sqlHost;
    }

    public String getSqlPort() {
        return sqlPort;
    }

    public String getSqlUsername() {
        return sqlUsername;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public String getSqlDatabase() {
        return sqlDatabase;
    }

    public boolean isSql() {
        return sql;
    }

    public boolean isDoKickonFail() {
        return doKickonFail;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public boolean isSavePlayerData() {
        return savePlayerData;
    }

    public int getSaveDuration() {
        return saveDuration;
    }

    public Messages getMessages() {
        return messages;
    }

    public boolean doBroadcastPlayerNewStreak() {
        return broadcastPlayerNewStreak;
    }

    public boolean isBroadcastTouchableRespect() {
        return broadcastTouchableRespect;
    }

    public boolean isPlaySoundOnBroadcast() {
        return playSoundOnBroadcast;
    }

    public Sound getBroadcastSound() {
        return broadcastSound;
    }
}
