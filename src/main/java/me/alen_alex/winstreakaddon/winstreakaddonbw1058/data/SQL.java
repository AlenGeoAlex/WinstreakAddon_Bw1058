package me.alen_alex.winstreakaddon.winstreakaddonbw1058.data;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.database.mysql.MySQL;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.DataStorage;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;

import java.sql.*;
import java.util.UUID;

public class SQL implements DataStorage {

    private WinstreakAddonBw1058 plugin;
    private MySQL mySQLConnection;
    private Connection connection;
    private final String JDBC_URL;

    public SQL(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        JDBC_URL = "jdbc:mysql://"+plugin.getPluginConfig().getSqlHost()+":"+plugin.getPluginConfig().getSqlPort()+"/"+plugin.getPluginConfig().getSqlDatabase()+"?autoReconnect=true";

    }

    @Override
    public boolean init() {
        try{
            mySQLConnection = new MySQL(JDBC_URL,plugin.getPluginConfig().getSqlUsername(),plugin.getPluginConfig().getSqlPassword());
            connection = mySQLConnection.getConnection();
            if(!connection.isClosed()) {
                if(initTables()) {
                    plugin.getLogger().info("Plugin is initialized with MySQL Storage system!");
                    return true;
                }
                else {
                    plugin.getLogger().severe("Failed to create tables for the plugin, Check the StackTrace for more errors!");
                    return false;
                }
            }else
                return false;
        }catch (SQLException e){
            plugin.getLogger().severe("Failed to init Database connection due to some reason. Check the stacktrace for more info");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean initTables() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `addonws` (`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(50) NOT NULL, `current` INT(3) NOT NULL, `highest` INT(3) NOT NULL );");
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isUserRegistered(UUID playerUUID) {
        boolean hasRegistered = false;
        try{
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM `addonws` WHERE `uuid` = ?;");
            ps.setString(1,playerUUID.toString());
            ResultSet set = ps.executeQuery();
            while (set.next()){
                hasRegistered = true;
                break;
            }
            set.close();
            ps.close();
            return hasRegistered;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registerUser(UUID playerUUID) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO `addonws` (`uuid`,`current`,`highest`) VALUES (?,?,?);");
            ps.setString(1,playerUUID.toString());
            ps.setInt(2,0);
            ps.setInt(3,0);
            ps.executeUpdate();
            ps.close();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Winstreak fetchUser(UUID playerUUID) {
        Winstreak winstreakObj = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM `addonws` WHERE `uuid` = ?");
            ps.setString(1,playerUUID.toString());
            ResultSet set = ps.executeQuery();
            while (set.next()){
                winstreakObj = new Winstreak(UUID.fromString(set.getString("uuid")),set.getInt("current"),set.getInt("highest"));
                break;
            }
            set.close();
            ps.close();
            return winstreakObj;
        }catch (SQLException e){
            e.printStackTrace();
            return winstreakObj;
        }
    }

    @Override
    public void addStreak(UUID playerUUID) {

    }

    private Connection getConnection(){
        try {
            if(connection == null || connection.isClosed())
                mySQLConnection = null;
                mySQLConnection = new MySQL(JDBC_URL,plugin.getPluginConfig().getSqlUsername(),plugin.getPluginConfig().getSqlPassword());
                connection = mySQLConnection.getConnection();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
