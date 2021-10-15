package me.alen_alex.winstreakaddon.winstreakaddonbw1058.data;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.DataStorage;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class SQLite implements DataStorage {

    private WinstreakAddonBw1058 plugin;
    private Connection connection;
    private final String JDBC_URL;

    public SQLite(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        this.JDBC_URL = "jdbc:sqlite:" + plugin.getDataFolder().getParent()+ File.separator+"BedWars1058"+File.separator+"Addons"+File.separator+"Winstreak" + "/database/winstreakData.db";
    }

    @Override
    public boolean init() {
        try {
            Class.forName("org.sqlite.JDBC");
            plugin.getFileUtils().generateFolder("database");
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
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `addonws` (`id` INTEGER PRIMARY KEY, `name` VARCHAR(30) NOT NULL, `uuid` VARCHAR(50) NOT NULL, `current` INT(3) NOT NULL, `highest` INT(3) NOT NULL );");
            System.out.println(ps.executeUpdate());
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
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = getConnection().prepareStatement("UPDATE `addonws` SET `current` = `current` + 1 WHERE `uuid` = ?;");
                    ps.setString(1, playerUUID.toString());
                    ps.executeUpdate();
                    ps.close();
                    ps.getConnection().close();
                } catch (SQLException e) {
                    plugin.getLogger().severe("Unable to add Streak for player " + playerUUID + ". Check Stacktrace for more info!");
                    e.printStackTrace();
                }
            }
        });
    }

    private Connection getConnection(){
        try {
            if(this.connection == null || this.connection.isClosed())
                this.connection = DriverManager.getConnection(JDBC_URL);
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
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = getConnection().prepareStatement("UPDATE `addonws` SET `current` = ?,`highest` = ? WHERE `uuid` = ?");
                    ps.setInt(1,obj.getCurrentStreak());
                    ps.setInt(2,obj.getBestStreak());
                    ps.setString(3,obj.getPlayerUUID().toString());
                    ps.executeUpdate();
                    ps.close();
                    ps.getConnection().close();
                }catch (SQLException e){
                    plugin.getLogger().info("Unable to save player data for "+obj.getPlayerUUID());
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @Override
    public void resetStreak(UUID uuid) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = getConnection().prepareStatement("UPDATE `addonws` SET `current` = ? WHERE `uuid` =?;");
                    ps.setInt(1,0);
                    ps.setString(2,uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                    ps.getConnection().close();
                }catch (SQLException e){
                    plugin.getLogger().severe("Unable to reset streak for player "+uuid+". Check stacktrace for possible errors!");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean setCurrent(UUID playerUUID, int currentStreak) {
        final boolean[] completed = {false};
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = getConnection().prepareStatement("UPDATE `addonws` SET `current` = ? WHERE `uuid` = ?;");
                    ps.setInt(1,currentStreak);
                    ps.setString(2,playerUUID.toString());
                    final int updateStatus = ps.executeUpdate();
                    if(updateStatus >= 1)
                        completed[0] = true;
                    else completed[0] = false;
                    ps.close();
                    ps.getConnection().close();
                }catch (SQLException e){
                    e.printStackTrace();
                    completed[0] = false;
                }
            }
        });
        return completed[0];
    }

    @Override
    public boolean setHighest(UUID playerUUID, int highestStreak) {
        final boolean[] completed = {false};
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = getConnection().prepareStatement("UPDATE `addonws` SET `highest` = ? WHERE `uuid` = ?;");
                    ps.setInt(1,highestStreak);
                    ps.setString(2,playerUUID.toString());
                    final int updateStatus = ps.executeUpdate();
                    if(updateStatus >= 1)
                        completed[0] = true;
                    else completed[0] = false;
                    ps.close();
                    ps.getConnection().close();
                }catch (SQLException e){
                    e.printStackTrace();
                    completed[0] = false;
                }
            }
        });
        return completed[0];
    }
}
