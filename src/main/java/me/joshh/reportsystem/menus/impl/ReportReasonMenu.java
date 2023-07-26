package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.util.Notification;
import me.joshh.reportsystem.util.ReasonItem;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ReportReasonMenu extends Menu {
    Player target;
    public ReportReasonMenu(PlayerMenuUtility playerMenuUtility, Player target) {
        super(playerMenuUtility);
        this.target = target;
    }

    @Override
    public String getMenuName() {
        return "Choose a reason for your report.";
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        String reason = "";
        switch (e.getCurrentItem().getType()) {
            case DIAMOND_SWORD:
                reason = "Hacking";
                break;
            case BOOK:
                reason = "Rudeness";
                break;
            case REDSTONE:
                reason = "Bug Abuse";
                break;

            case PAPER:
                reason = "Advertising";
                break;

            case NAME_TAG:
                reason = "Inappropriate Name";
                break;

            case LEATHER_CHESTPLATE:
                reason = "Inappropriate Skin";
                break;

            case CHAINMAIL_CHESTPLATE:
                reason = "Inappropriate Cape";
                break;

            case BOOK_AND_QUILL:
                reason = "Spamming";
                break;
            case SKULL:
                reason = "Racism";
                break;

            case BARRIER:
                reason = "Other";
                break;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = ReportSystem.myFormatObj;
        String formattedDate = now.format(myFormatObj);
        Player player = (Player) e.getWhoClicked();
        Report report = new Report(target, player, reason, formattedDate, ReportSystem.getInstance().getSQLManager().generateID(7));
        try {ReportSystem.getInstance().getSQLManager().createSQLReport(report); }
        catch (SQLException throwables) { throwables.printStackTrace(); }

        player.sendMessage("§a(!) Successfully reported " + target.getName() + " for " + reason + ".");
        try {
            ReportSystem.getInstance().getNotificationSQL().addNotification(new Notification((Player)e.getWhoClicked(), report, "CREATED", "PENDING"));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        player.closeInventory();

    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        ItemStack hackingItem = new ReasonItem(Material.DIAMOND_SWORD, "Hacking", "Unfair Advantage").getItem();
        ItemStack chatItem = new ReasonItem(Material.BOOK, "Rudeness", "Rudeness").getItem();
        ItemStack bugAbuseItem = new ReasonItem(Material.REDSTONE, "Bug Abuse", "Abusing a bug to gain an advantage").getItem();
        ItemStack advertisingItem = new ReasonItem(Material.PAPER, "Advertising", "Advertising").getItem();
        ItemStack inappropriateNameItem = new ReasonItem(Material.NAME_TAG, "Inappropriate Name", "Inappropriate Name").getItem();
        ItemStack inappropriateSkinItem = new ReasonItem(Material.LEATHER_CHESTPLATE, "Inappropriate Skin", "Inappropriate Skin").getItem();
        ItemStack inappropriateCapeItem = new ReasonItem(Material.CHAINMAIL_CHESTPLATE, "Inappropriate Cape", "Inappropriate Cape").getItem();
        ItemStack chatAbuseItem = new ReasonItem(Material.BOOK_AND_QUILL, "Spamming", "Spamming").getItem();
        ItemStack racismItem = new ReasonItem(Material.SKULL, "Racism", "Racism").getItem();

        ItemStack otherItem = new ReasonItem(Material.BARRIER, "Other", "Other").getItem();

        inventory.addItem(hackingItem, chatItem, bugAbuseItem, advertisingItem, inappropriateNameItem, inappropriateSkinItem, inappropriateCapeItem, chatAbuseItem, racismItem, otherItem);
    }


}
