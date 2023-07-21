package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.Report;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

public class ReportInfoMenu extends Menu {

    ReportSystem reportSystem = ReportSystem.getInstance();
    boolean isStaff;
    Report report;
    public ReportInfoMenu(PlayerMenuUtility playerMenuUtility, Report report, boolean isStaff) {
        super(playerMenuUtility);
        this.report = report;
        this.isStaff = isStaff;
    }

    @Override
    public String getMenuName() {
        return "Report Info";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(isStaff) {
            switch (e.getCurrentItem().getType()) {

                case BOOK_AND_QUILL:
                    new PunishmentMenu(reportSystem.getPlayerMenuUtility((Player) e.getWhoClicked()), report.getReportedUser()).open();
                    break;
                case BARRIER:
                    if (e.getCurrentItem().getItemMeta().getDisplayName() == "§cDeny this report.") {
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        Player player = (Player) e.getWhoClicked();
                        String[] newReason = new String[1];
                        player.sendMessage("§aPlease enter the new reason in the rename field.");
                        ReportSystem.sendSound(player);
                        new AnvilGUI(ReportSystem.getInstance(), player, "Edit reason here", (p, reply) -> {
                            System.out.println("hi");
                            newReason[0] = reply;

                            player.sendMessage("§aYou have changed the reason to: " + newReason[0]);
                            player.sendMessage("§aYou have denied the report.");
                            player.closeInventory();

                            ReportSystem.getInstance().getSQLManager().denyReport(report, newReason[0], player);
                            ReportSystem.notificationManager.sendDeniedReportNotification(report, player);

                            return newReason[0];
                        });


                    }
                    e.setCancelled(true);
            }
        }else {
            e.setCancelled(true);
        }
    }

    @Override
    public void setMenuItems() {
        Player reportedName = report.getReportedUser();

        ItemStack denyItem = new ItemStack(Material.BARRIER, 1, (short) 7);
        ItemStack reportInfo = new ItemStack(Material.PAPER);
        ItemStack punishItem = new ItemStack(Material.BOOK_AND_QUILL);

        ItemMeta infoMeta = reportInfo.getItemMeta();
        infoMeta.setDisplayName("§e " + reportedName.getName());
        ArrayList<String> infoLore = new ArrayList<>();



        infoLore.add(" ");
        infoLore.add("§7Status: " + (reportedName.isOnline() ? "§aOnline" : "§cOffline"));
        infoLore.add("§7Reported by: §a" + report.getReporter().getName());
        infoLore.add("§7Reported reason(s):");

        ArrayList<Report> reports = ReportSystem.activeReports.get(reportedName.getUniqueId().toString());
        for (Report r : reports) {
            infoLore.add("§7- §a" + r.getReason() + " §8(§e" + r.getID() + "§8)");
        }
        infoMeta.setLore(infoLore);

        reportInfo.setItemMeta(infoMeta);

        ItemMeta punishMeta = punishItem.getItemMeta();
        punishMeta.setDisplayName("§aSelect which punishment to inflict");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7Click to choose a punishment");
        lore.add("§7Choose to §6warn, §8mute, §ckick§7 or §4ban §7this user");
        punishMeta.setLore(lore);
        punishItem.setItemMeta(punishMeta);

        ItemMeta denyItemMeta = denyItem.getItemMeta();
        denyItemMeta.setDisplayName("§cDeny this report.");
        denyItem.setItemMeta(denyItemMeta);

        inventory.setItem(11, punishItem);
        inventory.setItem(13, reportInfo);
        inventory.setItem(15, denyItem);

        setFillerGlass();
    }
}
