package me.joshh.reportsystem.commands;

import me.joshh.reportsystem.commands.subcommands.SettingsCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class PluginCommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public PluginCommandManager(){
        subcommands.add(new SettingsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;

            if (args.length > 0){
                for (int i = 0; i < getSubcommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                        try {
                            getSubcommands().get(i).perform(p, args);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }else if(args.length == 0){
                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                for (int i = 0; i < getSubcommands().size(); i++){
                    p.sendMessage("§6" + getSubcommands().get(i).getSyntax() + "§r - §e" + getSubcommands().get(i).getDescription());
                }
                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
            }

        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

}

