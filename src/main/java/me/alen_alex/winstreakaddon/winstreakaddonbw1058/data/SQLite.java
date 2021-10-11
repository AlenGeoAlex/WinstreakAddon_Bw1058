package me.alen_alex.winstreakaddon.winstreakaddonbw1058.data;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.DataStorage;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;

import java.sql.*;
import java.util.UUID;

public class SQLite implements DataStorage {

    private WinstreakAddonBw1058 plugin;
    private Connection connection;
    private final String JDBC_URL;

    public SQLite(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        this.JDBC_URL = "jdbc:sqlite:" + plugin.getDataFolder() + "/database/auctionsData.db";
    }

    @Override
    public boolean init() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC_URL);
            if (!connection.isClosed()) {
                if (initTables()) {
                    plugin.getLogger().info("Plugin is intialized with SQLite Storage system!");
                    return true;
                } else {
                    plugin.getLogger().severe("Failed to create tables for the plugin, Check the StackTrace for more errors!");
                    return false;
                }
            } else
                return false;
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Failed to init Database connection due to some reason. Check the stacktrace for more info");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean initTables() {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `addonws` (`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY, `name` VARCHAR(30) NOT NULL, `uuid` VARCHAR(50) NOT NULL, `current` INT(3) NOT NULL, `highest` INT(3) NOT NULL );");
            ps.executeUpdate();
            ps.close();
            connection.close();
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
            connection.close();
            return hasRegistered;
        }catch (SQLException e){
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
            connection.close();
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
            connection.close();
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
                connection = DriverManager.getConnection(JDBC_URL);
        }catch (SQLException e) {
            e.printStackTrace();
        }
            return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if(!connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to close connection. Check stacktrace for more info!");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUserData(Winstreak obj) {

    }
}
