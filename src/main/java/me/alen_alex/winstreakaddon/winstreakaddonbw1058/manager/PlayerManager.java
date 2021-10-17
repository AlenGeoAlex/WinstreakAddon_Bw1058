package me.alen_alex.winstreakaddon.winstreakaddonbw1058.manager;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private WinstreakAddonBw1058 plugin;
    private List<Player> notificationPermission;

    public PlayerManager(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        notificationPermission = new ArrayList<Player>();
    }



    public List<Player> getNotificationPermission() {
        return notificationPermission;
    }
}
