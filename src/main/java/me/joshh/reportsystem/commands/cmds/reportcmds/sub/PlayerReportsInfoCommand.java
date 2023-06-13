package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerReportsInfoCommand extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "Shows all reports made on a certain player. Clicking on a report shows extra information.";
    }

    @Override
    public String getSyntax() {
        return "/reports player <username>";
    }

    @Override
    public void perform(Player p, String[] args) {
        HashMap<String, ArrayList<Report>> activeReports = ReportSystem.getActiveReports();
        Inventory reportInventory = Bukkit.createInventory(p, 54, "Your created reports");


        ItemStack grayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        glassMeta.setDisplayName(" ");
        grayGlass.setItemMeta(glassMeta);
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || (i + 1) % 9 == 0) {
                reportInventory.setItem(i, grayGlass);
            }
        }

        // Add the reports to the inventory
        int slot = 10;
        for (List<Report> reportList : activeReports.values()) {
            ItemBuilder book = new ItemBuilder(Material.BOOK_AND_QUILL);

            for (Report report : reportList) {
                Player reporter = Bukkit.getPlayer(UUID.fromString(report.getReporter()));
                if(reporter.getName() == p.getName()) {
                    Player target = Bukkit.getPlayer(UUID.fromString(report.getReportedUser()));

                    String onlineStatus = target.isOnline() ? "§a" : "§c";
                    book.setName(onlineStatus + target.getName());
                    try {
                        String timeAgo = report.getTimeSinceCreation();
                        book.addLoreLine("§7" + reporter.getName() + " ; §e" + report.getReason() + " §8(" + timeAgo + "ago) ; §7ID: §e" + report.getID());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            reportInventory.setItem(slot, book.toItemStack());
            slot++;

        }

        p.openInventory(reportInventory);
    }
}


