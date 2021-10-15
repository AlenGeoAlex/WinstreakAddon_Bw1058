package me.alen_alex.winstreakaddon.winstreakaddonbw1058.command.subcommand;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetCurrentCommand extends SubCommands {

    private List<String> aliases = new ArrayList<String>(){{
        add("setcur");
        add("sc");
    }};
    private List<String> allowedArgs = new ArrayList<String>(){{
        add("%players%");
    }};

    @Override
    public String getCommandName() {
        return "setcurrent";
    }

    @Override
    public String getCommandPermission() {
        return "ws.admin";
    }

    @Override
    public boolean registerTabCompleter() {
        return true;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean doRunFromConsole() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Used to set the current streak of a player";
    }

    @Override
    public String getSyntax() {
        return getCommandName()+" [playerName] [valueTo]";
    }

    @Override
    public void onCommand(Player player, String[] args) {
        System.out.println("Hello world");
    }

    @Override
    public List<String> getAllowedArgs() {
        return allowedArgs;
    }
}
