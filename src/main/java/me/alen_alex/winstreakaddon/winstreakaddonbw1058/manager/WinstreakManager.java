package me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WinstreakManager {

    private HashMap<Player, Winstreak> streaks = new HashMap<Player,Winstreak>();
    private PlayerManager playerManager;
    private WinstreakAddonBw1058 plugin;

    public boolean contains(Player player){
        return streaks.containsKey(player);
    }

    public WinstreakManager(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        playerManager = new PlayerManager(plugin);
    }

    /**
     * Checks were a uuid is cached in the streak HashMap
     * @param playerUUID of the player
     * @return true if contains, else false
     */
    public boolean contains(UUID playerUUID){
        if(Bukkit.getPlayer(playerUUID) != null){
            return streaks.containsKey(Bukkit.getPlayer(playerUUID));
        }return false;
    }

    /**
     * Add a player to the cache HashMap
     * @param player to add to the cache
     * @param streak the Winstreak Object of the player
     */
    public void insert(Player player, Winstreak streak){
        streaks.put(player,streak);
    }

    /**
     * Add a player to the cache HashMap
     * @param playerUUID to add to the cache
     * @param winstreak the Winstreak Object of the player
     */
    public void insert(UUID playerUUID,Winstreak winstreak){
        if(Bukkit.getPlayer(playerUUID) != null)
            insert(Bukkit.getPlayer(playerUUID),winstreak);
    }

    public void delete(Player player){
        streaks.remove(player);
    }

    public void delete(UUID playerUUID){
        if(Bukkit.getPlayer(playerUUID) != null)
            streaks.remove(Bukkit.getPlayer(playerUUID));
    }

    public Winstreak get(Player player){
        return streaks.get(player);
    }

    public void get(UUID playerUUID){
        if(Bukkit.getPlayer(playerUUID) != null)
            streaks.get(Bukkit.getPlayer(playerUUID));
    }

    /**
     * Save all current cached players of streaks HashMap to database
     * @return Time elapsed to save all players to database
     */
    public long saveAll(){
        long start = (System.currentTimeMillis());
        streaks.forEach(((player, winstreak) -> {winstreak.save();}));
        long end = (System.currentTimeMillis());
        return end-start;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
