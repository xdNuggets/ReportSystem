package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportCommand implements CommandExecutor {

    SQLManager sql = new SQLManager();
    String prefix = ReportSystem.prefix;


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
                DateTimeFormatter myFormatObj = ReportSystem.myFormatObj;
                String formattedDate = now.format(myFormatObj);


                // If the player is offline
                if(reportedPlayer == null) {

                    OfflinePlayer offlineTarget = player.getServer().getOfflinePlayer(args[0]);
                    System.out.println("Player " + offlineTarget.getName() + " is offline.");
                    if(offlineTarget.hasPlayedBefore()) {


                        // Send message to the player who created the report
                        sendOfflineAlert(player, reason, formattedDate, offlineTarget);


                        for (Player p : player.getServer().getOnlinePlayers()) {
                            if (p.hasPermission("rs.alert")) {
                                try {
                                    sendStaffAlert(p, reason, formattedDate, (Player) offlineTarget, player);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                ReportSystem.sendSound(p);

                            }
                        }
                        try {
                            sql.createReport((Player) offlineTarget, player, reason, formattedDate);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        player.sendMessage("§c(!) That player has never joined the network!");
                    }
                }else {
                    // If the player is online, create a report
                    System.out.println("Player " + reportedPlayer.getName() + " is online.");

                    sendAlert(player, reason, formattedDate, reportedPlayer);

                    for(Player p : player.getServer().getOnlinePlayers()) {
                        if(p.hasPermission("rs.alert")) {
                            // Send message to staff
                            try {
                                sendStaffAlert(p, reason, formattedDate, reportedPlayer, player);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            ReportSystem.sendSound(p);

                        }
                    }

                    try {
                        sql.createReport(player, reportedPlayer, reason, formattedDate);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private void sendOfflineAlert(Player player, String reason, String formattedDate, OfflinePlayer offlineTarget) {

        TextComponent message = new TextComponent("§A(!) You have reported " + offlineTarget.getName() + " for §e" + reason);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Information\n" +
                prefix + " §fReported by " + prefix + "§f " + player.getName() + "\n" +
                "\n" + prefix + " Reason " + prefix + " " + reason + "\n" +
                "\n" + prefix + " Reported at " + prefix + " " + formattedDate).create()));

        player.spigot().sendMessage(message);
    }

    private void sendAlert(Player player, String reason, String formattedDate, Player offlineTarget) {

        TextComponent message = new TextComponent("§A(!) You have reported " + offlineTarget.getName() + " for §e" + reason);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Information\n" +
                prefix + " §fReported by " + prefix + "§f " + player.getName() + "\n" +
                "\n" + prefix + " Reason " + prefix + " " + reason + "\n" +
                "\n" + prefix + " Reported at " + prefix + " " + formattedDate).create()));

        player.spigot().sendMessage(message);
    }

    private void sendStaffAlert(Player player, String reason, String formattedDate, Player reportedPlayer, Player reporter) throws SQLException {

        TextComponent message = new TextComponent("§e(!) " + reporter.getName() + " reported §c" + reportedPlayer.getName() + "§e for §a" + reason);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Information\n" +
                prefix + " §fReported by " + prefix + "§f " + reporter.getName() + "\n" +
                "\n" + prefix + " Reason " + prefix + " " + reason + "\n" +
                "\n" + prefix + " Reported at " + prefix + " " + formattedDate +
                "\n" + "§8Click to copy ID to clipboard").create()));

        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, new SQLManager().getID(reportedPlayer.getUniqueId().toString())));

        player.spigot().sendMessage(message);
    }


}
