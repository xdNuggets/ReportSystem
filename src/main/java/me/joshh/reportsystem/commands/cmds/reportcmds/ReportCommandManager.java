package me.joshh.reportsystem.commands.cmds.reportcmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.commands.cmds.reportcmds.sub.*;
import me.joshh.reportsystem.menus.impl.PlayerReportsInfoMenu;
import me.joshh.reportsystem.menus.impl.ReportsMenu;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class ReportCommandManager implements CommandExecutor {


    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public ReportCommandManager(){
        subcommands.add(new ReportInfoCommand());

        subcommands.add(new PlayerActiveReportsCommand());
        subcommands.add(new PlayerReportsInfoCommand());
        subcommands.add(new ReportCancelCommand());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        try {
                            getSubcommands().get(i).perform(p, args);
                        } catch (SQLException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("help")) {
                    p.sendMessage("§e§lReports Help");
                    p.sendMessage("§a-------------------------");
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        p.sendMessage("§e" + getSubcommands().get(i).getSyntax() + " §7- " + getSubcommands().get(i).getDescription());
                    }
                    p.sendMessage("§a-------------------------");
                }

            } else if (args.length == 0) {
                if (p.hasPermission("rs.manage")) {

                    new ReportsMenu(ReportSystem.getInstance().getPlayerMenuUtility(p)).open();
                } else {
                    p.sendMessage("§cYou do not have permission to use this command.");
                }


            }
        }


            return true;
        }


    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }
}
