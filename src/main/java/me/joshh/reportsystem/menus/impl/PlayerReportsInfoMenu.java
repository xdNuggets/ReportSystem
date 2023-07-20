package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PaginatedMenu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.Report;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class PlayerReportsInfoMenu extends PaginatedMenu {
    ArrayList<Report> reports;
    boolean allorNot;
    public PlayerReportsInfoMenu(PlayerMenuUtility playerMenuUtility, ArrayList<Report> reports, boolean allorNot) {
        super(playerMenuUtility);
        this.reports = reports;
        this.allorNot = allorNot;
    }


    @Override
    public String getMenuName() {
        return "All Reports";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        try {
            if(e.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL)) {
            new ReportInfoMenu(ReportSystem.getInstance().getPlayerMenuUtility((Player) e.getWhoClicked()), ReportSystem.getInstance().getSQLManager().getReportWithID(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))).open(); e.setCancelled(true); }
            else {
                e.setCancelled(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setMenuItems() throws ParseException {
        addMenuBorder();
        for (Report report : reports) {
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("§e" + report.getID());

            String timeAgo = report.getTimeSinceCreation();
            Player reporter = report.getReporter();

            ArrayList<String> lore = new ArrayList<>();
            if(allorNot) {
                lore.add("§7Reported User: §a" + report.getReportedUser().getName());
                lore.add("§7Reason: §a" + report.getReason());
                lore.add("§7Created: §a" + timeAgo + "ago");

            }else {
                lore.add("§7Reported By: §a" + reporter.getName());
                lore.add("§7Reason: §a" + report.getReason());
                lore.add("§7Created: §a" + timeAgo + "ago");
            }



            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            inventory.setItem(inventory.firstEmpty(), book);

        }
    }
}
