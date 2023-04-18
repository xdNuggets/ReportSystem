package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import me.joshh.reportsystem.sql.SQLManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static me.joshh.reportsystem.ReportSystem.activeReports;

public class ReportCommand implements CommandExecutor {

    SQLManager sql = new SQLManager();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            final SQLManager sql = new SQLManager();
            // /report <player> <reason>

            Player player = (Player) sender;

            if(!player.hasPermission("rs.report")) {
                player.sendMessage("§c(!) You do not have permission to use this command.");
                return true;
            }

            if(args.length == 0) {
                player.sendMessage("§c(!) Usage: /report <player> <reason>");
                return true;
            }

            if(args.length == 1) {
                player.sendMessage("§c(!) Usage: /report <player> <reason>");
                return true;
            }

            if(args.length >= 2) {
                // Sets reason to a string
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String reason = sb.toString();

                Player reportedPlayer = player.getServer().getPlayer(args[0]);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(ReportSystem.config.getString("messages.date-format"));
                String formattedDate = now.format(myFormatObj);

                // If the player is offline
                if(reportedPlayer == null) {

                    OfflinePlayer offlineTarget = player.getServer().getOfflinePlayer(args[0]);
                    System.out.println("Player " + offlineTarget.getName() + " is offline.");
                    if(offlineTarget.hasPlayedBefore()) {
                        Report report = new Report((Player) offlineTarget, player,reason, formattedDate);
                        player.sendMessage("§aReport created!");
                        player.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                        player.sendMessage("§eReported: §f" + report.getReportedUser().getName());
                        player.sendMessage("§eReason: §f" + report.getReason());
                        player.sendMessage("§e━━━━━━━━━━━━━━━━━━");

                        for(Player p : player.getServer().getOnlinePlayers()) {
                            if(p.hasPermission("rs.alert")) {
                                p.sendMessage("§eNew report created!");
                                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                                p.sendMessage("§eReported: §f" + report.getReportedUser().getName());
                                p.sendMessage("§eReason: §f" + report.getReason());
                                p.sendMessage("§eReported by: §f" + report.getReporter().getName());
                                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                                ReportSystem.sendSound(p);
                                addReport((Player) offlineTarget, player, reason, formattedDate);
                            }
                        }
                        try {

                            sql.createReport(reportedPlayer.getName(), player.getName(), reason, formattedDate);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        player.sendMessage("§c(!) That player has never joined the network!");
                    }
                }else {
                    // If the player is online, create a report
                    System.out.println("Player " + reportedPlayer.getName() + " is online.");
                    Report report = new Report(reportedPlayer, player,reason, formattedDate);
                    player.sendMessage("§aReport created!");
                    player.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                    player.sendMessage("§eReported: §f" + report.getReportedUser().getName());
                    player.sendMessage("§eReason: §f" + report.getReason());
                    player.sendMessage("§e━━━━━━━━━━━━━━━━━━");

                    for(Player p : player.getServer().getOnlinePlayers()) {
                        if(p.hasPermission("rs.alert")) {
                            p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                            p.sendMessage("§eReported: §f" + report.getReportedUser().getName());
                            p.sendMessage("§eReason: §f" + report.getReason());
                            p.sendMessage("§eReported by: §f" + report.getReporter().getName());
                            p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                            ReportSystem.sendSound(p);
                            addReport(reportedPlayer, player, reason, formattedDate);
                        }
                    }

                    try {
                        sql.createReport(reportedPlayer.getName(), player.getName(), reason, formattedDate);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public void addReport(Player reportedPlayer, Player reporter, String reason, String date) {
        if (!activeReports.containsKey(reportedPlayer.getName())) {
            activeReports.put(reportedPlayer.getName(), new ArrayList<>());
        }else{
            activeReports.get(reportedPlayer.getName()).add(new Report(reportedPlayer, reporter, reason, date));
        }

    }
}
