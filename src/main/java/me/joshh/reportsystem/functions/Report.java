package me.joshh.reportsystem.functions;

import org.bukkit.entity.Player;

import java.util.Random;

public class Report {


    private final String reported;
    private final String reason;
    private final String reporter;
    public static String id;
    String date;





    public Report(Player reported, Player reporter, String reason, String date, String id) {
        this.reported = reported.getUniqueId().toString();
        this.reason = reason;
        this.reporter = reporter.getUniqueId().toString();
        this.id = id;
        this.date = date;

    }

    public String getReportedUser() {
        return reported;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public String getReporter() {
        return reporter;
    }

    public String getID() {
        return id;
    }



}
