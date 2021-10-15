package me.alen_alex.winstreakaddon.winstreakaddonbw1058.command.subcommand;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommands {

    public abstract String getCommandName();

    public abstract String getCommandPermission();

    public abstract boolean registerTabCompleter();

    public abstract List<String> getAliases();

    public abstract boolean doRunFromConsole();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void onCommand(Player player,String[] args);

    public abstract List<String> getAllowedArgs();
}
