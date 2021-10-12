package me.alen_alex.winstreakaddon.winstreakaddonbw1058.object;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.api.events.PlayerNewBestStreak;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

public class Winstreak {


    private UUID playerUUID;
    private int currentStreak;
    private int bestStreak;
    private WinstreakAddonBw1058 plugin;

    public Winstreak( UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Winstreak(UUID playerUUID, int currentStreak, int bestStreak) {
        this.playerUUID = playerUUID;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.plugin = WinstreakAddonBw1058.getPlugin();
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }


    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void save(){
        Winstreak object = this;
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                WinstreakAddonBw1058.getPlugin().getDataStorage().saveUserData(object);
            }
        });
    }

    public void processWin(){
        final int newStreak = this.currentStreak + 1;
        if(newStreak > bestStreak){
            //TODO New bestStreak
            PlayerNewBestStreak event = new PlayerNewBestStreak(this.playerUUID,this,newStreak);
            plugin.getServer().getPluginManager().callEvent(event);
        }
        this.currentStreak = newStreak;
        plugin.getDataStorage().addStreak(this.playerUUID);
        //TODO Message for new streak
    }

    public void processFail(){
        plugin.getDataStorage().resetStreak(this.playerUUID);
        //TODO message while loosing streak
    }

    public boolean setCurrent(int value){
        return plugin.getDataStorage().setCurrent(this.playerUUID, value);
    }

    public boolean setHighest(int value){
        return plugin.getDataStorage().setHighest(this.playerUUID, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Winstreak winstreak = (Winstreak) o;
        return currentStreak == winstreak.currentStreak && bestStreak == winstreak.bestStreak && playerUUID.equals(winstreak.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID, currentStreak, bestStreak);
    }
}
