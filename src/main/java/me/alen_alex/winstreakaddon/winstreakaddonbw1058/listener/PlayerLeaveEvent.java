package me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    private WinstreakAddonBw1058 plugin;

    public PlayerLeaveEvent(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Registered Event: PlayerLeaveEvent");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.getStreakManager().contains(player)){
            plugin.getStreakManager().get(player).save(true);
            plugin.getStreakManager().delete(player);
        }

        if(!plugin.getStreakManager().getPlayerManager().getNotificationPermission().contains(player))
            plugin.getStreakManager().getPlayerManager().getNotificationPermission().remove(player);
    }

}
