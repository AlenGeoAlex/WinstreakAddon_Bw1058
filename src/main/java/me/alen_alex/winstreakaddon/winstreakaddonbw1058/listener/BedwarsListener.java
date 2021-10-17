package me.alen_alex.winstreakaddon.winstreakaddonbw1058.listener;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
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
                        final Winstreak player = plugin.getStreakManager().get(plugin.getServer().getPlayer(winnerUUID));
                        player.processWin();
                        player.setOnGame(false);
                        if(player.hasLeft()){
                            //TODO Complete it
                        }
                    } else plugin.getDataStorage().addStreak(winnerUUID);
                });
                losers.forEach((loserUUID) -> {
                    if(plugin.getStreakManager().contains(loserUUID) && plugin.getServer().getPlayer(loserUUID) != null){
                        final Winstreak player = plugin.getStreakManager().get(plugin.getServer().getPlayer(loserUUID));
                       player.processFail();
                       player.setOnGame(false);
                       if(player.hasLeft()){
                           //TODO Complete it
                        }
                    }else plugin.getDataStorage().resetStreak(loserUUID);
                });
            }
        });
    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event){
        if(event.getNewState() == GameState.playing ){
            final IArena gameArena = event.getArena();
            gameArena.getPlayers().forEach((player -> {
                if(plugin.getStreakManager().contains(player)){
                    plugin.getStreakManager().get(player).setOnGame(true);
                }
            }));
        }

    }

}
