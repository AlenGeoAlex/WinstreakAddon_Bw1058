package me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener;

import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class BedwarsListener implements Listener {

    private WinstreakAddonBw1058 plugin;

    public BedwarsListener(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Registered Event: BedwarsListener");
    }

    @EventHandler
    public void onGameEndEvent(GameEndEvent event){
        final List<UUID> winners = event.getWinners();
        final List<UUID> losers = event.getLosers();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                winners.forEach(winnerUUID -> {
                    if (plugin.getStreakManager().contains(winnerUUID) && plugin.getServer().getPlayer(winnerUUID) != null) {
                        plugin.getStreakManager().get(plugin.getServer().getPlayer(winnerUUID)).processWin();
                    } else plugin.getDataStorage().addStreak(winnerUUID);
                });
                losers.forEach((loserUUID) -> {
                    if(plugin.getStreakManager().contains(loserUUID) && plugin.getServer().getPlayer(loserUUID) != null){
                        plugin.getStreakManager().get(plugin.getServer().getPlayer(loserUUID)).processFail();
                    }else plugin.getDataStorage().resetStreak(loserUUID);
                });
            }
        });
    }

}
