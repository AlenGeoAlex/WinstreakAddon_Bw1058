package me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils.PermissionData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinEvents implements Listener {

    private WinstreakAddonBw1058 plugin;

    public PlayerJoinEvents(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Registered Event: PlayerJoinEvent");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
    plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
        @Override
        public void run() {
            Winstreak playerStreak = plugin.getDataStorage().fetchUser(player.getUniqueId());
            if(playerStreak == null){
                plugin.getLogger().warning("Unable to load player data for user "+player.getName());
                if(plugin.getPluginConfig().isDoKickonFail())
                    player.kickPlayer(plugin.getPluginConfig().getKickMessage());
                return;
            }
            if(plugin.getStreakManager().contains(player)) {
                plugin.getLogger().warning("There seems to already have a data loaded for the user " + player.getName() + ". Overwriting it!");
                plugin.getStreakManager().delete(player);
            }
            plugin.getStreakManager().insert(player,playerStreak);
        }
    },20L);

    if(player.hasPermission(PermissionData.NOTIFICATION_PERMISSION.getPermission())){
        if(!plugin.getStreakManager().getPlayerManager().getNotificationPermission().contains(player))
            plugin.getStreakManager().getPlayerManager().getNotificationPermission().add(player);
        if(plugin.isForcedSQLite())
            plugin.getMessageUtils().sendMessage(player,"&cAn unknown error occured to init MySQL as storage system, Plugin is now using its fallback system &6(SQLite)",false);
    }

    }

    @EventHandler
    public void onAsyncPlayerJoin(AsyncPlayerPreLoginEvent event){
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        UUID joinPlayerUUID = event.getUniqueId();
        if(!plugin.getDataStorage().isUserRegistered(joinPlayerUUID)){
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    if(!plugin.getDataStorage().registerUser(joinPlayerUUID)){
                        plugin.getLogger().warning("Unable to register user "+joinPlayerUUID+" on to the database!");
                        if(plugin.getPluginConfig().isDoKickonFail()){
                            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                            event.setKickMessage(plugin.getPluginConfig().getKickMessage());
                        }
                    }
                }
            });
        }
    }

}
