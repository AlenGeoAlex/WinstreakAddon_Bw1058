package me.alen_alex.winstreakaddon.winstreakaddonbw1058.command;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.WinstreakAddonBw1058;
import me.alen_alex.winstreakaddon.winstreakaddonbw1058.command.subcommand.SubCommands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Command extends org.bukkit.command.Command implements CommandExecutor, TabCompleter {

    private final WinstreakAddonBw1058 plugin;
    private final String commandName,commandPermission;
    private HashMap<String, SubCommands> subCommandsHashMap;
    private HashMap<String,String> aliases;

    public Command(WinstreakAddonBw1058 plugin, String commandName, String commandPermission) {
        super(commandName);
        this.plugin = plugin;
        this.commandName = commandName;
        this.commandPermission = commandPermission;
        subCommandsHashMap = new HashMap<String, SubCommands>();
        aliases = new HashMap<String, String>();
        plugin.getLogger().info("Registered command: "+commandName);
    }

    public void register(){
        Field commandField = null;
        try {
        commandField = plugin.getServer().getClass().getDeclaredField("commandMap");
        commandField.setAccessible(true);
        CommandMap commandMap = null;
        commandMap = (CommandMap) commandField.get(plugin.getServer());
        commandMap.register(commandName, this);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void registerSubCommand(String commandName,SubCommands subCommand){
        subCommandsHashMap.put(commandName,subCommand);
        subCommand.getAliases().forEach((a) -> {
            aliases.put(a,commandName);
        });
        plugin.getLogger().info("Registered Subcommand for "+commandName+": "+subCommand.getCommandName());
    }


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return null;
    }

    public void noPermission(CommandSender sender){
        plugin.getMessageUtils().sendMessage(sender,"&cYou don't have enough permission to run this command!",false);
    }

    public void cantRunFromConsole(CommandSender sender){
        sender.sendMessage("This command cannot be run from console!");
    }

    public void unknownCommand(CommandSender sender){
        plugin.getMessageUtils().sendMessage(sender,"&cUnknown command",false);
        helpMessage(sender);
    }

    public void helpMessage(CommandSender sender){
        /*plugin.getMessageUtils().sendMessage(sender,"&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----",false);
        plugin.getMessageUtils().sendMessage(sender,"&e&l     Winstreak Addon",false);
        plugin.getMessageUtils().sendMessage(sender,"&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----",false);*/
        subCommandsHashMap.forEach((key,value) -> {
            plugin.getMessageUtils().sendMessage(sender,"&a"+value.getSyntax()+" &7- &b"+value.getDescription()+" &8- &6"+value.getCommandPermission(),false);
        });
        plugin.getMessageUtils().sendMessage(sender,"&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----&b&l-----&6&l-----",false);
    }

    public List<String> parsePlayers(String regexCharacter){
        List<String> tabCompletion = new ArrayList<String>();
            plugin.getServer().getOnlinePlayers().forEach((playerName) ->{
                tabCompletion.add(playerName.getName());
        });
        return tabCompletion;
    }


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if(args.length == 0){
                helpMessage(sender);
            }else{
                if(aliases.containsKey(args[0])){
                    final SubCommands currentCommand = subCommandsHashMap.get(aliases.get(args[0]));
                    System.out.println(args[0] +" -- "+subCommandsHashMap.containsKey(aliases.get(args[0])));
                    if(!(sender instanceof Player) && !currentCommand.doRunFromConsole()){
                        cantRunFromConsole(sender);
                        return true;
                    }
                    if(!sender.hasPermission(currentCommand.getCommandPermission())){
                        noPermission(sender);
                        return true;
                    }
                    currentCommand.onCommand((Player) sender,args);
                }else{
                    unknownCommand(sender);
                    return true;
                }
            }
        return true;
    }
}
