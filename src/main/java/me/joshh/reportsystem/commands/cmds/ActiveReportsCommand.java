package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActiveReportsCommand implements CommandExecutor {

    // chatgpt is a fucking livesaver
    // this code was generated by chatgpt and it actually works

    public static HashMap<String, ArrayList<Report>> activeReports = ReportSystem.activeReports;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("rs.manage")) {
                Inventory reportInventory = Bukkit.createInventory(p, 54, "Unhandled Reports");

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
                for (String player : activeReports.keySet()) {
                    ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
                    ItemMeta meta = book.getItemMeta();
                    String onlineStatus = Bukkit.getPlayer(player) != null ? "§a" : "§c";
                    meta.setDisplayName(onlineStatus + player);
                    List<String> lore = new ArrayList<>();
                    for (Report report : activeReports.get(player)) {
                        String date = report.getDate();
                        SimpleDateFormat myFormatObj = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String now = LocalDateTime.now().format(ReportSystem.myFormatObj);

                        try {
                            Date date2 = myFormatObj.parse(date);
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
                            Player reporter = report.getReporter();

                            System.out.println(report);
                            System.out.println("--" + report.getReportedUser());
                            System.out.println(". " + report.getReporter());
                            lore.add("§7" + reporter.getName() + " / §e" + report.getReason() + " §8(" + timeAgo + "ago)");


                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    meta.setLore(lore);
                    book.setItemMeta(meta);
                    reportInventory.setItem(slot, book);
                    slot++;
                }

                p.openInventory(reportInventory);
            }
        }
        return false;
    }






}
