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
            new ReportInfoMenu(ReportSystem.getInstance().getPlayerMenuUtility((Player) e.getWhoClicked()), ReportSystem.getInstance().getSQLManager().getReportWithID(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), false).open(); e.setCancelled(true); }
            else {
                e.setCancelled(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
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
                if (!((index + 1) >= reports.size())) {
                    page = page + 1;
                    super.open();
                } else {
                    e.getWhoClicked().sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }


        }
    }

    @Override
    public void setMenuItems() throws ParseException {
        addMenuBorder();

        for (int i = 0; i < getMaxItemsPerPage(); i++) {


            int index = getMaxItemsPerPage() * page + i;
            if (index >= reports.size()) {
                break;
            }
            if (reports.get(index) == null) {
                continue;
            }

            Report report = reports.get(index);
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("§e" + report.getID());

            String timeAgo = report.getTimeSinceCreation();
            Player reporter = report.getReporter();

            ArrayList<String> lore = new ArrayList<>();
            String line1 = allorNot ? "§7Reported: §a" + report.getReportedUser().getName() : "§7Reporter: §a" + reporter.getName();
            lore.add(line1);
            lore.add("§7Reason: §a" + report.getReason());
            lore.add("§7Created: §a" + timeAgo + "ago");

            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);


            inventory.addItem(book); // add statement
        }
    }
}
