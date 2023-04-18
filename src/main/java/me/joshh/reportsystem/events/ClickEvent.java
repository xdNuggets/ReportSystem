package me.joshh.reportsystem.events;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import me.joshh.reportsystem.sql.SQLManager;
import net.wesjd.anvilgui.AnvilGUI;
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
        SQLManager sql = new SQLManager();
        ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemStack denyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);

        if(e.getCurrentItem() == null) return;


        // Method called when player clicks on an item in the Reports Inventory (See ActiveReportsCommand.java)
        if (e.getInventory().getTitle().equalsIgnoreCase("Unhandled Reports") && e.getCurrentItem().getType() == Material.BOOK) {
            e.setCancelled(true);
            String reporterName = e.getCurrentItem().getItemMeta().getDisplayName().replace("§7", "");
            Report report = sql.getReportByReporter(reporterName);
            assert report != null;
            System.out.println(report);

            if(e.getCurrentItem().getType() == Material.BOOK) {
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 27, "§7" + report.getReporter().getName() + "'s Report");

                ItemStack reportInfo = new ItemStack(Material.PAPER);
                ItemStack punishItem = new ItemStack(Material.BOOK_AND_QUILL);
                ItemMeta borderMeta = borderItem.getItemMeta();

                borderMeta.setDisplayName("§7");
                borderItem.setItemMeta(borderMeta);


                ItemMeta infoMeta = reportInfo.getItemMeta();
                infoMeta.setDisplayName("§eReport §a#" + sql.getID(report.getReportedUser().getName()));
                infoMeta.setLore(e.getCurrentItem().getItemMeta().getLore());
                reportInfo.setItemMeta(infoMeta);

                ItemMeta punishMeta = punishItem.getItemMeta();
                punishMeta.setDisplayName("§aSelect which punishment to inflict");
                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7Click to choose a punishment");
                lore.add("§7Choose to §6warn, §8mute, §ckick or §4ban this user");
                punishMeta.setLore(lore);
                punishItem.setItemMeta(punishMeta);

                ItemMeta denyItemMeta = denyItem.getItemMeta();
                denyItemMeta.setDisplayName("§cDeny this report.");
                denyItem.setItemMeta(denyItemMeta);

                inv.setItem(11, punishItem);
                inv.setItem(13, reportInfo);
                inv.setItem(15, denyItem);

                for(int i = 0; i == inv.getSize() - 3; i++) {
                    inv.setItem(inv.firstEmpty(), borderItem);
                }

                e.getWhoClicked().openInventory(inv);

            }
        }

        // Method called when player clicks on an item in an active report inventory
        if(e.getInventory().getTitle().contains("'s Report")) {
            String reportedUser = e.getInventory().getTitle().replace("§7", "").replace("'s Report", "");
            Player reportedPlayer = Bukkit.getPlayer(reportedUser);
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.BARRIER) {
                e.getWhoClicked().closeInventory();
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 45, "§7" + reportedUser + "'s Report");

                ItemStack warnItem = new ItemStack(Material.PAPER);
                ItemStack muteItem = new ItemStack(Material.BOOK_AND_QUILL);
                ItemStack kickItem = new ItemStack(Material.RABBIT_FOOT);
                ItemStack banItem = new ItemStack(Material.BARRIER);

                ItemMeta warnMeta = warnItem.getItemMeta();
                ItemMeta muteMeta = muteItem.getItemMeta();
                ItemMeta kickMeta = kickItem.getItemMeta();
                ItemMeta banMeta = banItem.getItemMeta();

                warnMeta.setDisplayName("§Warn " + reportedUser);
                muteMeta.setDisplayName("§8Mute " + reportedUser);
                kickMeta.setDisplayName("§cKick " + reportedUser);
                banMeta.setDisplayName("§4Ban " + reportedUser);







            }
            if(e.getCurrentItem() == denyItem) {
                e.setCancelled(true);
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 9, "Would you like to edit the reason?");
                ItemStack yesItem = new ItemStack(Material.EMERALD_BLOCK);
                ItemStack noItem = new ItemStack(Material.REDSTONE_BLOCK);
                ItemStack infoItem = new ItemStack(Material.PAPER);
                ItemMeta infoMeta = infoItem.getItemMeta();
                infoMeta.setDisplayName("§eReason: " + sql.getReportByReported(reportedUser).getReason());
                ItemMeta yesMeta = yesItem.getItemMeta();
                ItemMeta noMeta = noItem.getItemMeta();
                yesMeta.setDisplayName("§aYes.");
                noMeta.setDisplayName("§cNo.");
                yesItem.setItemMeta(yesMeta);
                noItem.setItemMeta(noMeta);
                inv.setItem(1, yesItem);
                inv.setItem(7, noItem);

                e.getWhoClicked().openInventory(inv);
                // 0 1 2 3 4 5 6 7 8
            }

            if(e.getInventory().getTitle().equals("Would you like to edit the reason?")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                    e.getWhoClicked().closeInventory();

                    String[] newReason = new String[1];

                    new AnvilGUI(ReportSystem.plugin, (Player) e.getWhoClicked(), "Edit Reason", (player, reply) -> {
                        newReason[0] = reply;

                        e.getWhoClicked().sendMessage("§aYou have changed the reason to: " + newReason[0]);
                        // Handle the ban here.
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ReportSystem.config.getString("commands.ban-command").replace("%player%", reportedUser).replace("%reason%", newReason[0]));
                        return null;
                    });

                }
                if(e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§aYou have denied the report.");

                }
            }
        }


        if(e.getInventory().getTitle().equalsIgnoreCase("Report System Settings")) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.WATCH) {
                ReportSystem.config.set("toggleable.show-date", !ReportSystem.config.getBoolean("toggleable.show-date"));
                p.sendMessage("§aThe date is now:  " + ReportSystem.config.getBoolean("toggleable.show-date"));
            }

            if(e.getCurrentItem().getType() == Material.NOTE_BLOCK) {
                ReportSystem.config.set("toggleable.sounds", !ReportSystem.config.getBoolean("toggleable.sounds"));
                p.sendMessage("§aSounds are now: " + ReportSystem.config.getBoolean("toggleable.sounds"));
            }

            if(e.getCurrentItem().getType() == Material.PAPER) {
                ReportSystem.config.set("toggleable.alert", !ReportSystem.config.getBoolean("toggleable.alert"));
                p.sendMessage("§aAlerts are now: " + ReportSystem.config.getBoolean("toggleable.alert"));
            }

            if(e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                ReportSystem.config.set("toggleable.use-ban-command", !ReportSystem.config.getBoolean("toggleable.use-ban-command"));
                p.sendMessage("§aBan command is now: " + ReportSystem.config.getBoolean("toggleable.use-ban-command"));
            }

            if(e.getWhoClicked() instanceof Player) {
                ((Player) e.getWhoClicked()).updateInventory();
            }


        }

    }
}
