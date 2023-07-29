package me.joshh.reportsystem.util;

import org.bukkit.entity.Player;

public class Notification {

    private Player player;
    private Report report;
    private String oldStatus, newStatus;


    public Notification(Player player, Report report, String oldStatus, String newStatus) {
        this.player = player;
        this.report = report;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;

    }

    public Player getPlayer() {
        return player;
    }

    public Report getReport() {
        return report;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public String toString() {
        if(oldStatus.equals("CREATED")) {
            return "§7You created a new report! ID: §e" + report.getID();
        }else {
            return "§7Report ID: §e" + report.getID() + " §7; Status changed from §e" + oldStatus + "§7 to §e" + newStatus;
        }
    }
}
