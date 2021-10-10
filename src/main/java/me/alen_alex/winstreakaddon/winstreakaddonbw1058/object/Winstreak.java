package me.alen_alex.winstreakaddon.winstreakaddonbw1058.object;
import java.util.HashMap;
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
}
