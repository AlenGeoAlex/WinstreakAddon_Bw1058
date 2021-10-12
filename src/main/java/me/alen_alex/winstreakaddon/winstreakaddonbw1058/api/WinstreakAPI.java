package me.alen_alex.winstreakaddon.winstreakaddonbw1058.api;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.plugin.java.JavaPlugin;

public class WinstreakAPI {

    private JavaPlugin apiRequester;
    private WinstreakAddonBw1058 plugin;
    public WinstreakAPI(JavaPlugin apiRequester) {
        this.apiRequester = apiRequester;
        this.plugin = WinstreakAddonBw1058.getPlugin();
        apiRequester.getLogger().info("The API has been hooked in succesfully with "+apiRequester.getDescription().getName());
    }

    public WinstreakAddonBw1058 getPlugin() {
        return plugin;
    }
}
