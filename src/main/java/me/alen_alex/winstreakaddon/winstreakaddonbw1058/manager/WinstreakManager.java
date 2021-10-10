package me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WinstreakManager {

    private HashMap<Player, Winstreak> streaks = new HashMap<Player,Winstreak>();

    public boolean contains(Player player){
        return streaks.containsKey(player);
    }

    public boolean contains(UUID playerUUID){
        if(Bukkit.getPlayer(playerUUID) != null){
            return streaks.containsKey(Bukkit.getPlayer(playerUUID));
        }return false;
    }

    public void insert(Player player, Winstreak streak){
        streaks.put(player,streak);
    }

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

}
