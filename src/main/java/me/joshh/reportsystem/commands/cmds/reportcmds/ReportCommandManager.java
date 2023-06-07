package me.joshh.reportsystem.commands.cmds.reportcmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.commands.cmds.reportcmds.sub.ReportInfoCommand;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class ReportCommandManager implements CommandExecutor {


    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public ReportCommandManager(){
        subcommands.add(new ReportInfoCommand());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HashMap<String, ArrayList<Report>> activeReports = ReportSystem.getActiveReports();

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        try {
                            getSubcommands().get(i).perform(p, args);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else if (args.length == 0) {
                if (p.hasPermission("rs.manage")) {
                    System.out.println("command has been run :)");
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
                    int reportNum = 0;
                    for (List<Report> reportList : activeReports.values()) {
                        ItemBuilder book = new ItemBuilder(Material.BOOK_AND_QUILL);
                        Player target = Bukkit.getPlayer(UUID.fromString(reportList.get(reportNum).getReportedUser()));
                        String onlineStatus = target.isOnline() ? "§a" : "§c";
                        book.setName(onlineStatus + target.getName());

                        for (Report report : reportList) {
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
                                Player reporter = Bukkit.getPlayer(UUID.fromString(report.getReporter()));
                                if (reporter == null) {
                                    reporter = (Player) Bukkit.getOfflinePlayer(report.getReporter());
                                }

                                book.addLoreLine("§7" + reporter.getName() + " ; §e" + report.getReason() + " §8(" + timeAgo + "ago) ; §7ID: §e" + report.getID());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        reportInventory.setItem(slot, book.toItemStack());
                        slot++;
                        reportNum++;
                    }

                        p.openInventory(reportInventory);
                    }
                }


            }

        
        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }
}
