package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import me.joshh.reportsystem.sql.SQLFunctions;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportCommand implements CommandExecutor {

    SQLFunctions sql = new SQLFunctions();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
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

            for(int i = 1; i < Report.reports.size(); i++) {
                if(Report.reports.get(i).getReported().getName().equalsIgnoreCase(args[0])) {
                    player.sendMessage("§c(!) This player has already been reported!");
                    return true;
                }
            }


            if(args.length >= 2) {
                // Sets reason to a string
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String reason = sb.toString();

                Player target = player.getServer().getPlayer(args[0]);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(ReportSystem.config.getString("messages.date-format"));
                String formattedDate = now.format(myFormatObj);

                // If the player is offline
                if(target == null) {
                    OfflinePlayer offlineTarget = player.getServer().getOfflinePlayer(args[0]);
                    if(offlineTarget.hasPlayedBefore()) {
                        Report report = new Report((Player) offlineTarget, reason, player, formattedDate);
                        player.sendMessage("§aReport created!");
                        player.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                        player.sendMessage("§eReported: §f" + report.getReported().getName());
                        player.sendMessage("§eReason: §f" + report.getReason());
                        player.sendMessage("§e━━━━━━━━━━━━━━━━━━");

                        for(Player p : player.getServer().getOnlinePlayers()) {
                            if(p.hasPermission("rs.alert")) {
                                p.sendMessage("§eNew report created!");
                                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                                p.sendMessage("§eReported: §f" + report.getReported().getName());
                                p.sendMessage("§eReason: §f" + report.getReason());
                                p.sendMessage("§eReported by: §f" + report.getReporter().getName());
                                p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                                ReportSystem.sendSound(p);
                            }

                        }
                    }else {
                        player.sendMessage("§c(!) That player has never joined the network!");
                    }
                }else {
                    // If the player is online, create a report
                    Report report = new Report(target, reason, player, formattedDate);
                    player.sendMessage("§aReport created!");
                    player.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                    player.sendMessage("§eReported: §f" + report.getReported().getName());
                    player.sendMessage("§eReason: §f" + report.getReason());
                    player.sendMessage("§e━━━━━━━━━━━━━━━━━━");

                    for(Player p : player.getServer().getOnlinePlayers()) {
                        if(p.hasPermission("rs.alert")) {
                            p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                            p.sendMessage("§eReported: §f" + report.getReported().getName());
                            p.sendMessage("§eReason: §f" + report.getReason());
                            p.sendMessage("§eReported by: §f" + report.getReporter().getName());
                            p.sendMessage("§e━━━━━━━━━━━━━━━━━━");
                            ReportSystem.sendSound(p);
                        }
                    }
                }
            }
        }
        return false;
    }
}
