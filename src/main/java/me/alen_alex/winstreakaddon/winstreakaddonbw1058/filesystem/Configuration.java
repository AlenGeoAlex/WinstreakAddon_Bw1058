package me.alen_alex.winstreakaddon.winstreakaddonbw1058.filesystem;

import de.leonhard.storage.Config;
import de.leonhard.storage.Yaml;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.YamlFiles;
import org.apache.commons.lang3.StringUtils;

public class Configuration implements YamlFiles {

    private WinstreakAddonBw1058 plugin;
    private Config pluginConfiguration;
    private String version;
    //
    private String sqlHost,sqlPort,sqlUsername,sqlPassword,sqlDatabase;
    private boolean sql,doKickonFail;
    private String kickMessage;

    public Configuration(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        init();
    }

    @Override
    public void init() {
        pluginConfiguration = plugin.getFileUtils().createConfiguration();
        version = pluginConfiguration.getOrSetDefault("version","1.0");
        plugin.getLogger().info("Configuration has been intiated with version "+version);
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
            plugin.getLogger().warning("Configuration related to SQL are missing, Falling back to SQLite!");
            sql = false;
        }
        doKickonFail = pluginConfiguration.getBoolean("settings.kick-when-failed-to-register-a-player");
        kickMessage = plugin.getMessageUtils().parseColor(pluginConfiguration.getString("settings.kick-on-failed-message"));

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
}
