package me.alen_alex.winstreakaddon.winstreakaddonbw1058.api.events;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerNewBestStreak extends Event {

    private UUID player;
    private Winstreak winstreak;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private int bestStreak;

    public PlayerNewBestStreak(UUID player, Winstreak winstreak, int bestStreak) {
        this.player = player;
        this.winstreak = winstreak;
        this.bestStreak = bestStreak;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public UUID getPlayer() {
        return player;
    }

    public Winstreak getWinstreak() {
        return winstreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }
}
