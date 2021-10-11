package me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class PlayerManager {

    private WinstreakAddonBw1058 plugin;

    public PlayerManager(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
    }

    private List<Player> notificationPermission;

    public List<Player> getNotificationPermission() {
        return notificationPermission;
    }
}
