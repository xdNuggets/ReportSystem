package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class StatisticsMenu extends Menu {
    public StatisticsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Your Statistics";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        ItemStack deniedReportsItem = new ItemStack(Material.REDSTONE);
        ItemMeta meta = deniedReportsItem.getItemMeta();
        meta.setDisplayName("§cDenied Reports");
        ArrayList<String> lore = new ArrayList<>();
        boolean isStaff = playerMenuUtility.getOwner().hasPermission("");
        lore.add(isStaff ? "§aYou have denied §e " + getAmountofReportsDenied(true) + " §areports." : "§e" + getAmountofReportsDenied(false) + " §aof your reports have been denied.");
        meta.setLore(lore);
        deniedReportsItem.setItemMeta(meta);
        inventory.setItem(11, deniedReportsItem);

        ItemStack acceptedReportsItem = new ItemStack(Material.EMERALD);
        ItemMeta meta2 = acceptedReportsItem.getItemMeta();
        meta2.setDisplayName("§aAccepted Reports");
        ArrayList<String> lore2 = new ArrayList<>();
        lore.add(isStaff ? "§aYou have denied §e " + getAmountofReportsAccepted(true) + " §areports." : "§e" + getAmountofReportsAccepted(false) + " §aof your reports have been denied.");

        lore2.add("§aYou have accepted §e " + 0 + " §areports.");
        meta2.setLore(lore2);
        acceptedReportsItem.setItemMeta(meta2);
        inventory.setItem(15, acceptedReportsItem);



    }


    public int getAmountofReportsDenied(boolean isStaff) {
        try {
            String query = isStaff ? "SELECT * FROM denied_reports WHERE denied_by = ?" : "SELECT * FROM denied_reports WHERE reporter = ?";
            PreparedStatement ps = ReportSystem.getInstance().getSQL().getConnection().prepareStatement(query);
            ps.setString(1, playerMenuUtility.getOwner().getUniqueId().toString());
            return ps.getFetchSize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getAmountofReportsAccepted(boolean isStaff) {
        try {
            String query = isStaff ? "SELECT * FROM accepted_reports WHERE denied_by = ?" : "SELECT * FROM accepted_reports WHERE reporter = ?";
            PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement(query);
            ps.setString(1, playerMenuUtility.getOwner().getUniqueId().toString());
            return ps.getFetchSize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
