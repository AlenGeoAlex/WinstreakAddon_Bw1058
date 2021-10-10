package me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

    private String prefix;
    private WinstreakAddonBw1058 plugin;

    public MessageUtils(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
        this.prefix = "";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String parseColor(String input) {
        if (input == null)
            return null;
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public void sendMessage(Player player, String message, boolean showPrefix) {
        if (message == null)
            return;
        if (showPrefix && !prefix.isEmpty())
            player.sendMessage(prefix + parseColor(message));
        else
            player.sendMessage(parseColor(message));
    }

    public void sendMessage(CommandSender sender, String message, boolean showPrefix) {
        if (message == null)
            return;
        if (showPrefix  && !prefix.isEmpty())
            sender.sendMessage(prefix + parseColor(message));
        else
            sender.sendMessage(parseColor(message));
    }

    public void sendMessageNoPrefix(Player player, String message) {
        if (message == null)
            return;
        player.sendMessage(parseColor(message));
    }

    public void sendMessageNoPrefix(CommandSender sender, String message) {
        if (message == null)
            return;
        sender.sendMessage(message);
    }

    public void broadcastMessageNoPrefix(String message) {
        if (message == null)
            return;
        Bukkit.getServer().broadcastMessage(parseColor(message));
    }


    public void broadcastMessage(String message) {
        if (message == null)
            return;
        Bukkit.getServer().broadcastMessage(prefix + parseColor(message));
    }

    public void sendJSONSuggestMessage(Player player, String Message, String SuggestionCommand, String HoverText, boolean ShowPrefix) {
        if (Message == null)
            return;
        TextComponent tc = new TextComponent();
        tc.setText(parseColor(Message));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, SuggestionCommand));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage((BaseComponent) tc);
    }

    public void sendJSONSuggestMessage(CommandSender sender, String Message, String SuggestionCommand, String HoverText, boolean ShowPrefix) {
        if (Message == null)
            return;
        if (!(sender instanceof Player))
            return;
        Player player = (Player) sender;
        TextComponent tc = new TextComponent();
        if (ShowPrefix  && !prefix.isEmpty())
            tc.setText(prefix + parseColor(Message));
        else
            tc.setText(parseColor(Message));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, SuggestionCommand));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage((BaseComponent) tc);
    }

    public void sendJSONExecuteCommand(Player player, String Message, String RunCommand, String HoverText, boolean ShowPrefix) {
        if (Message == null)
            return;
        TextComponent tc = new TextComponent();
        tc.setText(parseColor(Message));
        tc.setText(prefix + parseColor(Message));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, RunCommand));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage(((BaseComponent) tc));
    }

    public void sendJSONLink(Player player, String Message, String redirectTo, String HoverText) {
        if (Message == null)
            return;
        TextComponent tc = new TextComponent();
        tc.setText(parseColor(Message));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, redirectTo));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage(((BaseComponent) tc));
    }

    public void sendJSONExecuteCommand(CommandSender sender, String Message, String RunCommand, String HoverText, boolean ShowPrefix) {
        if (Message == null)
            return;
        if (!(sender instanceof Player))
            return;
        Player player = (Player) sender;
        TextComponent tc = new TextComponent();
        if (ShowPrefix  && !prefix.isEmpty())
            tc.setText(prefix + parseColor(Message));
        else
            tc.setText(parseColor(Message));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, RunCommand));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage(((BaseComponent) tc));
    }

    public void sendJSONHoverMessage(Player player, String Message, String HoverText, boolean ShowPrefix) {
        if (Message == null)
            return;
        TextComponent tc = new TextComponent();
        if (ShowPrefix  && !prefix.isEmpty())
            tc.setText(prefix + parseColor(Message));
        else
            tc.setText(parseColor(Message));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(parseColor(HoverText)).create())));
        player.spigot().sendMessage((BaseComponent) tc);
    }

    public void sendBroadcastMessage(String message, boolean showPrefix) {
        if (message == null)
            return;
        if (showPrefix  && !prefix.isEmpty())
            Bukkit.getServer().broadcastMessage(prefix + parseColor(message));
        else
            Bukkit.getServer().broadcastMessage(parseColor(message));
    }



}
