package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.NotificationSQL;
import me.joshh.reportsystem.util.Notification;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.SQLException;

public class NotificationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            NotificationSQL notificationSQL = ReportSystem.getInstance().getNotificationSQL();
            try {
                for(Notification notification : notificationSQL.getNotifications(player)) {
                    player.sendMessage(notification.toString());
                    notificationSQL.removeNotification(notification);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
