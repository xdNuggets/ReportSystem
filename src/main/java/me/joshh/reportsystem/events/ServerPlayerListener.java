package me.joshh.reportsystem.events;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.NotificationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class ServerPlayerListener implements Listener {

    NotificationManager notificationManager = ReportSystem.getInstance().getNotificationManager();



    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        if(notificationManager.getNotifications(player).size() > 0) {
            player.sendMessage("You have " + notificationManager.getNotifications(player).size() + " new notifications!");
            player.sendMessage("Use /notifications to view them.");

        }

    }


}
