package me.alen_alex.winstreakaddon.winstreakaddonbw1058.filesystem;

import de.leonhard.storage.Yaml;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces.YamlFiles;

public class Messages implements YamlFiles {

    private WinstreakAddonBw1058 plugin;
    private Yaml messageConfig;
    private String version;
    //
    private String playerNewStreak,streakBackToZero,normalBroadcastMessage,advancedBroadcastMessage,advancedBroadcastHoverMessage,advancedBroadcastReply;

    public Messages(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        messageConfig = plugin.getFileUtils().createFile(plugin.getResource("lang.yml"),"lang.yml");
        if(messageConfig != null){
            loadFile();
            version = messageConfig.getOrSetDefault("version",plugin.getDescription().getVersion());
        }else plugin.getLogger().severe("Unable to load messages. Possible reason failed to create/read lang.yml");
    }

    @Override
    public void loadFile() {
        playerNewStreak = plugin.getMessageUtils().parseColor(messageConfig.getString("new-streak"));
        streakBackToZero = plugin.getMessageUtils().parseColor(messageConfig.getString("lose-streak"));
        normalBroadcastMessage = plugin.getMessageUtils().parseColor(messageConfig.getString("broadcast-streak.normal-broadcast"));
        advancedBroadcastMessage = plugin.getMessageUtils().parseColor(messageConfig.getString("broadcast-streak.advanced.broadcast"));
        advancedBroadcastHoverMessage = plugin.getMessageUtils().parseColor(messageConfig.getString("broadcast-streak.advanced.hover-message"));
        advancedBroadcastReply = plugin.getMessageUtils().parseColor(messageConfig.getString("broadcast-streak.advanced.respect-paid"));
        messageConfig = null;
    }

    @Override
    public Yaml getFile() {
        return plugin.getFileUtils().createFile(plugin.getResource("lang.yml"),"lang.yml");
    }

    @Override
    public void reloadFile() {
        init();
    }

    @Override
    public String getFileVersion() {
        return version;
    }

    public String getPlayerNewStreak(String playerName,int newStreak) {
        return playerNewStreak.replaceAll("%player_name%",playerName).replaceAll("%newstreak%",String.valueOf(newStreak));
    }

    public String getNormalBroadcastMessage(String playerName,int newStreak) {
        return normalBroadcastMessage.replaceAll("%player_name%",playerName).replaceAll("%newstreak%",String.valueOf(newStreak));
    }

    public String getAdvancedBroadcastMessage(String playerName,int newStreak) {
        return advancedBroadcastMessage.replaceAll("%player_name%",playerName).replaceAll("%newstreak%",String.valueOf(newStreak));
    }

    public String getAdvancedBroadcastReply(String repliedPlayer) {
        return advancedBroadcastReply.replaceAll("%player_name%",repliedPlayer);
    }

    public String getAdvancedBroadcastHoverMessage() {
        return advancedBroadcastHoverMessage;
    }

    public String getStreakBackToZero() {
        return streakBackToZero;
    }
}
