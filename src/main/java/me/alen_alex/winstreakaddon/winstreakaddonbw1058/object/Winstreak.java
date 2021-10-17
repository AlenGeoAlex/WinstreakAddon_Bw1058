package me.alen_alex.winstreakaddon.winstreakaddonbw1058.object;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.api.events.PlayerNewBestStreak;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class Winstreak {
    private UUID playerUUID;
    private int currentStreak;
    private int bestStreak;
    private boolean onGame;
    private boolean left;
    private WinstreakAddonBw1058 plugin;

    public Winstreak( UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Winstreak(UUID playerUUID, int currentStreak, int bestStreak) {
        this.playerUUID = playerUUID;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.onGame = false;
        this.left = false;
        this.plugin = WinstreakAddonBw1058.getPlugin();
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
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

    public boolean isOnGame() {
        return onGame;
    }

    public void setOnGame(boolean onGame) {
        this.onGame = onGame;
    }

    public boolean hasLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void processWin(){
        final int newStreak = this.currentStreak++;
        if(newStreak > bestStreak){
            PlayerNewBestStreak event = new PlayerNewBestStreak(this.playerUUID,this,newStreak);
            plugin.getServer().getPluginManager().callEvent(event);
            if(Bukkit.getPlayer(playerUUID) != null) {
                Player player = Bukkit.getPlayer(playerUUID);
                if(plugin.getPluginConfig().doBroadcastPlayerNewStreak()){
                    if(plugin.getPluginConfig().isBroadcastTouchableRespect()){
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                TextComponent component = plugin.getPluginConfig().getMessages().getClickableBroadcast(newStreak,player);
                                Bukkit.getOnlinePlayers().forEach((serverPlayer) -> {
                                    serverPlayer.spigot().sendMessage(component);
                                    if(plugin.getPluginConfig().isPlaySoundOnBroadcast())
                                        serverPlayer.playSound(serverPlayer.getLocation(),plugin.getPluginConfig().getBroadcastSound(),1.0F,1.0F);
                                });
                            }
                        });
                    }else{
                            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    if (plugin.getPluginConfig().isPlaySoundOnBroadcast()) {
                                        Bukkit.getOnlinePlayers().forEach((serverPlayer1) -> {
                                            serverPlayer1.sendMessage(plugin.getPluginConfig().getMessages().getNormalBroadcastMessage(player.getName(), newStreak));
                                            serverPlayer1.playSound(serverPlayer1.getLocation(),plugin.getPluginConfig().getBroadcastSound(),1.0F,1.0F);
                                        });
                                    }else {
                                        plugin.getMessageUtils().broadcastMessageNoPrefix(plugin.getPluginConfig().getMessages().getNormalBroadcastMessage(player.getName(),newStreak));
                                    }
                                }
                            });
                        }
                }
            }
        }
        this.currentStreak = newStreak;
        plugin.getDataStorage().addStreak(this.playerUUID);
        if(Bukkit.getPlayer(playerUUID) != null){
            Player player = Bukkit.getPlayer(playerUUID);
            plugin.getMessageUtils().sendMessage(player, plugin.getPluginConfig().getMessages().getPlayerNewStreak(player.getName(),currentStreak),false);
        }
    }

    public void processFail(){
        plugin.getDataStorage().resetStreak(this.playerUUID);
        if(Bukkit.getPlayer(playerUUID) != null){
            plugin.getMessageUtils().sendMessage(Bukkit.getPlayer(playerUUID), plugin.getPluginConfig().getMessages().getStreakBackToZero(),false);
        }
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
