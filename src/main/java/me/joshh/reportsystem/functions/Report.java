package me.joshh.reportsystem.functions;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Report {




    private final Player reported;
    private final String reason;
    private final Player reporter;
    public static int id = 1;
    String date;





    public Report(Player reported, Player reporter, String reason, String date)   {
        this.reported = reported;
        this.reason = reason;
        this.reporter = reporter;

        this.date = date;
        id++;

    }

    public Player getReportedUser() {
        return reported;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public Player getReporter() {
        return reporter;
    }

    public int getID() {
        return id;
    }



}
