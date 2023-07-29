package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.NotificationManager;
import me.joshh.reportsystem.util.Notification;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class NotificationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            NotificationManager notificationManager = ReportSystem.getInstance().getNotificationManager();
            int amount = 10;



            if (strings.length == 1) {
                amount = Integer.parseInt(strings[0]);
            }

            try {
                if(notificationManager.getNotifications(player).isEmpty()) {
                    player.sendMessage("§aYou have no notifications!");
                    return false;
                }else {
                    int i = 0;
                    int notificationAmount = notificationManager.getNotifications(player).size();
                    if (amount > notificationAmount) {
                        amount = notificationAmount;
                    }
                    for (Notification notification : notificationManager.getNotifications(player)) {

                        if (i != amount) {
                            System.out.println(notification.getReport());
                            player.sendMessage(notification.toString());
                            notificationManager.removeNotification(notification);
                            i++;
                        }
                    }
                    player.sendMessage("§aShowing §e" + i + "§a of §e" + notificationAmount + "§a notifications. Use /notifications <amount> to show more");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
