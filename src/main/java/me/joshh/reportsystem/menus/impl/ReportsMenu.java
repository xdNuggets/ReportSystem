package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.PaginatedMenu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReportsMenu extends PaginatedMenu {

    HashMap<String, ArrayList<Report>> activeReports = ReportSystem.getActiveReports();

    public ReportsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Unhandled Reports";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
            String reportedName = e.getCurrentItem().getItemMeta().getDisplayName();
            reportedName = reportedName.startsWith("§c") ? reportedName.replace("§c", "") : reportedName.replace("§a", "");

            Player p = (Bukkit.getPlayer(reportedName).isOnline() ? Bukkit.getPlayer(reportedName) : (Player) Bukkit.getOfflinePlayer(reportedName));
            Report report = null;
            try {
                report = SQLManager.getReportByReported(p.getName());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert report != null;

            new ReportInfoMenu(ReportSystem.getPlayerMenuUtility((Player) e.getWhoClicked()), report).open();
        }

        if (e.getCurrentItem().getType().equals(Material.BARRIER)) {

            //close inventory
            e.getWhoClicked().closeInventory();

        } else if (e.getCurrentItem().getType().equals(Material.WOOD_BUTTON)) {
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                if (page == 0) {
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "You are already on the first page.");
                } else {
                    page = page - 1;
                    super.open();
                }
            } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                if (!((index + 1) >= activeReports.size())) {
                    page = page + 1;
                    super.open();
                } else {
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }


        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        int slot = 10;
        for (List<Report> reportList : activeReports.values()) {
            ItemBuilder book = new ItemBuilder(Material.BOOK_AND_QUILL);

            for (Report report : reportList) {
                Player target = Bukkit.getPlayer(UUID.fromString(report.getReportedUser()));

                String onlineStatus = target.isOnline() ? "§a" : "§c";
                book.setName(onlineStatus + target.getName());



                try {
                    String timeAgo = report.getTimeSinceCreation();
                    Player reporter = Bukkit.getPlayer(UUID.fromString(report.getReporter()));
                    if (reporter == null) {
                        reporter = (Player) Bukkit.getOfflinePlayer(report.getReporter());
                    }

                    book.addLoreLine("§7" + reporter.getName() + " ; §e" + report.getReason() + " §8(" + timeAgo + "ago) ; §7ID: §e" + report.getID());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            inventory.setItem(slot, book.toItemStack());
            slot++;

        }
    }
}
