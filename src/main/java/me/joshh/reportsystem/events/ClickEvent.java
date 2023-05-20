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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ClickEvent implements Listener {



    @EventHandler
    public void onClickEvent(InventoryClickEvent e) throws SQLException {

        ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemStack denyItem = new ItemStack(Material.BARRIER, 1, (short) 7);





        // Method called when player clicks on an item in the Reports Inventory (See ActiveReportsCommand.java)
        if (e.getInventory().getTitle().equalsIgnoreCase("Unhandled Reports")) {

            e.setCancelled(true);
            String reportedName = e.getCurrentItem().getItemMeta().getDisplayName();
            reportedName = reportedName.startsWith("§c") ? reportedName.replace("§c", "") : reportedName.replace("§a", "");

            Player p = Bukkit.getPlayer(reportedName);
            Report report = SQLManager.getReportByReported(Bukkit.getPlayer(reportedName).getName());
            assert report != null;
            System.out.println(report);


            if (e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {

                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 27, "§7" + reportedName + "'s Report");

                ItemStack reportInfo = new ItemStack(Material.PAPER);
                ItemStack punishItem = new ItemStack(Material.BOOK_AND_QUILL);
                ItemMeta borderMeta = borderItem.getItemMeta();

                borderMeta.setDisplayName("§7");
                borderItem.setItemMeta(borderMeta);

                ItemMeta infoMeta = reportInfo.getItemMeta();
                infoMeta.setDisplayName("§e " + reportedName);
                ArrayList<String> infoLore = new ArrayList<>();

                SimpleDateFormat myFormatObj = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String now = LocalDateTime.now().format(ReportSystem.myFormatObj);

                try {
                    Date date2 = myFormatObj.parse(report.getDate());
                    Date date1 = myFormatObj.parse(now);

                    long diff = date1.getTime() - date2.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    String timeAgo = "";
                    if (diffDays > 0) {
                        timeAgo += diffDays + "d, ";
                    }
                    if (diffHours > 0) {
                        timeAgo += diffHours + "h, ";
                    }
                    if (diffMinutes > 0) {
                        timeAgo += diffMinutes + "m, ";
                    }
                    if (diffSeconds > 0) {
                        timeAgo += diffSeconds + "s ";
                    }
                    if (timeAgo.equals("")) {
                        timeAgo = "0s";
                    }
                    infoLore.add("§7Report ID: §6" + report.getID());
                    infoLore.add(" ");
                    infoLore.add("§7Reported by: §a" + Bukkit.getPlayer(UUID.fromString(report.getReporter())).getName());

                    infoLore.add("§7Reported on: §a" + report.getDate() + " §8" + timeAgo + "ago)");
                    infoLore.add("§7Reported reason(s):");

                    ArrayList<Report> reports = ReportSystem.activeReports.get(p.getUniqueId().toString());
                    for (Report r : reports) {
                        infoLore.add("§7- §a" + r.getReason());
                    }
                    infoMeta.setLore(infoLore);



                } catch (ParseException f) {
                    throw new RuntimeException(f);
                }


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

                inv.setItem(11, punishItem);
                inv.setItem(13, reportInfo);
                inv.setItem(15, denyItem);

                for (int i = 0; i == inv.getSize() - 3; i++) {
                    inv.setItem(inv.firstEmpty(), borderItem);
                }

                e.getWhoClicked().openInventory(inv);

            }
        } else if (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) e.setCancelled(true);

        // Method called when player clicks on an item in an active report inventory
        if(e.getInventory().getTitle().contains("'s Report")) {
            String reportedUser = e.getInventory().getTitle().replace("§7", "").replace("'s Report", "");
            Report report = SQLManager.getReportByReported(reportedUser);
            assert report != null;

            e.setCancelled(true);


            // calls if the report is to be accepted
            if (e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                e.getWhoClicked().closeInventory();
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), 27, "§7" + reportedUser + "'s Report");

                ItemStack warnItem = new ItemStack(Material.PAPER);
                ItemStack muteItem = new ItemStack(Material.BOOK_AND_QUILL);
                ItemStack kickItem = new ItemStack(Material.RABBIT_FOOT);
                ItemStack banItem = new ItemStack(Material.BARRIER);

                ItemMeta warnMeta = warnItem.getItemMeta();
                ItemMeta muteMeta = muteItem.getItemMeta();
                ItemMeta kickMeta = kickItem.getItemMeta();
                ItemMeta banMeta = banItem.getItemMeta();

                warnMeta.setDisplayName("§7Warn " + reportedUser);
                muteMeta.setDisplayName("§8Mute " + reportedUser);
                kickMeta.setDisplayName("§cKick " + reportedUser);
                banMeta.setDisplayName("§4Ban " + reportedUser);

                warnItem.setItemMeta(warnMeta);
                muteItem.setItemMeta(muteMeta);
                kickItem.setItemMeta(kickMeta);
                banItem.setItemMeta(banMeta);

                inv.setItem(10, warnItem);
                inv.setItem(12, muteItem);
                inv.setItem(14, kickItem);
                inv.setItem(16, banItem);

                for (int i = 0; i == inv.getSize() - 4; i++) {
                    inv.setItem(inv.firstEmpty(), borderItem);
                }

                e.getWhoClicked().openInventory(inv);

            }

            switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§7Warn":
                    String warnReason = changeReason((Player) e.getWhoClicked());
                    // Warn stuff
                    SQLManager.acceptReport(report, warnReason);

                case "§8Mute":

                    String muteReason = changeReason((Player) e.getWhoClicked());
                    // Mute stuff
                    SQLManager.acceptReport(report, muteReason);

                case "§cKick":
                    String kickReason = changeReason((Player) e.getWhoClicked());
                    // Kick stuff
                    SQLManager.acceptReport(report, kickReason);

                case "§4Ban":
                    String banReason = changeReason((Player) e.getWhoClicked());
                    // Ban stuff
                    SQLManager.acceptReport(report, banReason);
            }



            // Denial Stuff
            if(e.getCurrentItem().getItemMeta().getDisplayName() == "§cDeny this report.") {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                Player player = (Player) e.getWhoClicked();
                String[] newReason = new String[1];
                player.sendMessage("§aPlease enter the new reason in the rename field.");
                ReportSystem.sendSound(player);
                new AnvilGUI(ReportSystem.plugin, player, "Edit reason here", (p, reply) -> {
                    System.out.println("hi");
                    newReason[0] = reply;

                    player.sendMessage("§aYou have changed the reason to: " + newReason[0]);
                    player.sendMessage("§aYou have denied the report.");
                    player.closeInventory();

                    SQLManager.denyReport(report, newReason[0]);

                    return newReason[0];
                });


            }


        }




        // /rs settings stuff
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


    private String changeReason(Player player) {
        String[] newReason = new String[1];
        player.sendMessage("§aPlease enter the new reason in the rename field.");
        ReportSystem.sendSound(player);
        new AnvilGUI(ReportSystem.plugin, player, "Edit reason here", (p, reply) -> {
            System.out.println("hi");
            newReason[0] = reply;

            player.sendMessage("§aYou have changed the reason to: " + newReason[0]);

            return newReason[0];
        });

        return ReportSystem.config.getString("messages.default-reason");
    }




}
