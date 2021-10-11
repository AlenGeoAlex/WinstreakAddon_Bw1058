package me.alen_alex.winstreakaddon.winstreakaddonbw1058.task;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class SaveDataTask extends BukkitRunnable {

    private WinstreakAddonBw1058 plugin;

    public SaveDataTask(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(plugin.getServer().getOnlinePlayers().isEmpty())
            return;

        long elapsedTime = plugin.getStreakManager().saveAll();
        plugin.getLogger().info("Auto save was completed! Took "+elapsedTime+" ms");
    }

    public void cancel(){
        if(this.getTaskId() > 0)
            try {
                this.cancel();
            }catch (IllegalStateException e){}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveDataTask that = (SaveDataTask) o;
        return Objects.equals(plugin, that.plugin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin);
    }
}
