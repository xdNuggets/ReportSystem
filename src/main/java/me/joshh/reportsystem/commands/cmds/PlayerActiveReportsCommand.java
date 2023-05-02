package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerActiveReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            HashMap<String, ArrayList<Report>> reports = ReportSystem.activeReports;


            List<Report> reportsByReporter = new ArrayList<>();
            for (List<Report> reportList : reports.values()) {
                for (Report report : reportList) {
                    if (report.getReporter().equals(p.getName())) {
                        reportsByReporter.add(report);
                    }
                }
            }


            // DO THE EXACT SAME SHIT AS ACTIVEREPORTSCOMMAND.JAVA BUT FOR THIS FUCKER AHHHHHHHHHHHHHHH


        }
        return false;
    }
}
