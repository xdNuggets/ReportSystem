package me.joshh.reportsystem.util;

import me.joshh.reportsystem.ReportSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Report {


    private final String reported;
    private final String reason;
    private final String reporter;
    private final String id;
    private final String date;

    private String status;


    public Report(Player reported, Player reporter, String reason, String date, String id) {
        this.reported = reported.getUniqueId().toString();
        this.reason = reason;
        this.reporter = reporter.getUniqueId().toString();
        this.id = id;
        this.date = date;
        this.status = "PENDING";

    }

    public Player getReportedUser() {
        return Bukkit.getPlayer(UUID.fromString(reported));
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public Player getReporter() {
        return Bukkit.getPlayer(UUID.fromString(reporter));
    }

    public String getID() {
        return id;
    }

    public String getTimeSinceCreation() throws ParseException {
        String date = this.getDate();
        SimpleDateFormat myFormatObj = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String now = LocalDateTime.now().format(ReportSystem.myFormatObj);
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
        return timeAgo;
    }

}
