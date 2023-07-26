package me.joshh.reportsystem.sql;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.util.Notification;
import me.joshh.reportsystem.util.Report;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationSQL {

    private MySQL sql = ReportSystem.getInstance().getSQL();

    public void createTable() throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS notifications (reportID VARCHAR(10) PRIMARY KEY, oldStatus VARCHAR(10), newStatus VARCHAR(10), player VARCHAR(128))");
        ps.executeUpdate();

    }

    public void addNotification(Notification noti) throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO notifications (reportID, oldStatus, newStatus, player) VALUES (?, ?, ?, ?)");
        ps.setString(1, noti.getReport().getID());
        ps.setString(2, noti.getOldStatus());
        ps.setString(3, noti.getNewStatus());
        ps.setString(4, noti.getPlayer().getUniqueId().toString());
        ps.executeUpdate();

    }

    public void removeNotification(Notification noti) throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("DELETE FROM notifications WHERE reportID=?");
        ps.setString(1, noti.getReport().getID());
        ps.executeUpdate();

    }

    public ArrayList<Notification> getNotifications(Player player) throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM notifications WHERE player=?");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        ArrayList<Notification> notifications = new ArrayList<>();
        while(ps.getResultSet().next()) {
            notifications.add(new Notification(player, new SQLManager().getReportWithID(ps.getResultSet().getString("reportID")), ps.getResultSet().getString("oldStatus"), ps.getResultSet().getString("newStatus")));
        }
        return notifications;
    }

}
