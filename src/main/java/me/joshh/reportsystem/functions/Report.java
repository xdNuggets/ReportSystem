package me.joshh.reportsystem.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Report {


    private final String reported;
    private final String reason;
    private final String reporter;
    public static int id = 1;
    String date;


    public Report(Player reported, Player reporter, String reason, String date)   {
        this.reported = reported.getUniqueId().toString();
        this.reason = reason;
        this.reporter = reporter.getUniqueId().toString();

        this.date = date;
        id++;

    }

    public Player getReportedUser() {
        return Bukkit.getPlayer(reported);
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public Player getReporter() {

        return Bukkit.getPlayer(reporter);
    }

    public int getID() {
        return id;
    }



}
