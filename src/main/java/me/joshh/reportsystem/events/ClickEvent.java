package me.joshh.reportsystem.events;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import me.joshh.reportsystem.sql.SQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;

public class ClickEvent implements Listener {

    private FileConfiguration config = ReportSystem.config;

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) throws SQLException {
        SQLFunctions sqlFunctions = new SQLFunctions();
        ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemStack comingSoon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);

        // Method called when player clicks on an item in the Reports Inventory (See ActiveReportsCommand.java)
        if (e.getInventory().getTitle().equalsIgnoreCase("Unhandled Reports")) {
            e.setCancelled(true);
            System.out.println(e.getCurrentItem().getItemMeta().getDisplayName().replace("§eReport §6#", ""));
            Report report = Report.getReport(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§eReport §6#", "")));
            assert report != null;
            System.out.println(report);

            if(e.getCurrentItem().getType() == Material.BOOK) {
                System.out.println("Report #" + sqlFunctions.getID(report.getReported().getName()) + " clicked.");
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 27, "Report #" + sqlFunctions.getID(report.getReported().getName()));

                ItemStack reportInfo = new ItemStack(Material.PAPER);
                ItemStack banItem = new ItemStack(Material.BARRIER);
                ItemMeta borderMeta = borderItem.getItemMeta();

                borderMeta.setDisplayName("§7");
                borderItem.setItemMeta(borderMeta);


                ItemMeta infoMeta = reportInfo.getItemMeta();
                infoMeta.setDisplayName("§eReport §a#" + sqlFunctions.getID(report.getReported().getName()));
                infoMeta.setLore(e.getCurrentItem().getItemMeta().getLore());
                reportInfo.setItemMeta(infoMeta);

                ItemMeta banMeta = banItem.getItemMeta();
                banMeta.setDisplayName("§cBan this user §7(Permanent).");
                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7Click to ban this user");
                lore.add("§7Default: " + config.getString("messages.ban-item-click"));
                banMeta.setLore(lore);
                banItem.setItemMeta(banMeta);

                ItemMeta comingSoonMeta = comingSoon.getItemMeta();
                comingSoonMeta.setDisplayName("§7Coming soon...");
                comingSoon.setItemMeta(comingSoonMeta);

                inv.setItem(11, banItem);
                inv.setItem(13, reportInfo);
                inv.setItem(15, comingSoon);

                for(int i = 0; i == inv.getSize() - 3; i++) {
                    inv.setItem(inv.firstEmpty(), borderItem);
                }

                e.getWhoClicked().openInventory(inv);

            }
        }

        // Method called when player clicks on an item in the Report Inventory
        if(e.getInventory().getTitle().contains("Report #")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.BARRIER) {
                e.getWhoClicked().closeInventory();
                String banMessage = config.getString("messages.ban-command");
                Report report = Report.getReport(Integer.parseInt(e.getInventory().getTitle().replace("Report #", "")));

                String modifiiedBanMessage = banMessage.replace("%player%", e.getWhoClicked().getName()).replace("%reason%",report.getReason());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), modifiiedBanMessage);
                e.getWhoClicked().sendMessage("§aSuccessfully banned the user!");
                sqlFunctions.deleteReport(sqlFunctions.getID(report.getReported().getName()));

            }

            if(e.getCurrentItem() == comingSoon) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage("§c(!) This feature is coming soon!");
            }
        }


        if(e.getInventory().getTitle().equalsIgnoreCase("Report System Settings")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.WATCH) {
                ReportSystem.config.set("toggleable.show-date", !ReportSystem.config.getBoolean("toggleable.show-date"));
            }

            if(e.getCurrentItem().getType() == Material.NOTE_BLOCK) {
                ReportSystem.config.set("toggleable.sounds", !ReportSystem.config.getBoolean("toggleable.sounds"));
            }

            if(e.getCurrentItem().getType() == Material.PAPER) {
                ReportSystem.config.set("toggleable.alert", !ReportSystem.config.getBoolean("toggleable.alert"));
            }

            if(e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                ReportSystem.config.set("toggleable.use-ban-command", !ReportSystem.config.getBoolean("toggleable.use-ban-command"));
            }

            if(e.getWhoClicked() instanceof Player) {
                ((Player) e.getWhoClicked()).updateInventory();
            }


        }

    }
}
