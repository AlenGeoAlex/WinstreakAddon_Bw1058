package me.alen_alex.winstreakaddon.winstreakaddonbw1058.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("wsadmin|winstreakadmin")
public class WSAdmin extends BaseCommand {

    private WinstreakAddonBw1058 plugin;

    public WSAdmin(WinstreakAddonBw1058 plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public static void onHelpCommand(CommandSender sender, CommandHelp help){
        help.showHelp();
    }

    @Subcommand("set")
    public static void onSet(Player player){

    }



}
