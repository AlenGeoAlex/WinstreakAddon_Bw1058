package me.alen_alex.winstreakaddon.winstreakaddonbw1058.object;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Winstreak {


    private UUID playerUUID;
    private int currentStreak;
    private int bestStreak;

    public Winstreak( UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Winstreak(UUID playerUUID, int currentStreak, int bestStreak) {
        this.playerUUID = playerUUID;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
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

    public void addStreak(){

    }

    public void save(boolean destroy){
        Winstreak object = this;
        Bukkit.getScheduler().runTaskAsynchronously(WinstreakAddonBw1058.getPlugin(), new Runnable() {
            @Override
            public void run() {
                WinstreakAddonBw1058.getPlugin().getDataStorage().saveUserData(object);
            }
        });
        if(destroy) {
            try {
                finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
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
