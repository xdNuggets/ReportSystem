package me.joshh.reportsystem.functions;

import me.joshh.reportsystem.sql.SQLFunctions;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;

public class Report {


    public static HashMap<Integer, Report> reports = new HashMap<>();

    private final Player player;
    private final String reason;
    private final Player reporter;
    public static int id = 1;

    String date;


    private boolean active;


    public Report(Player player, String reason, Player reporter, String date)   {
        this.player = player;
        this.reason = reason;
        this.reporter = reporter;
        this.active = true;
        this.id = id;
        this.date = date;

        reports.put(id, this);
        try {
            final SQLFunctions sql = new SQLFunctions();
            sql.createReport(reporter.getName(), player.getName(), reason);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        id++;

    }

    public Player getReported() {
        return player;
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

    public static HashMap<Integer, Report> getReports() {
        return reports;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



    public static Report getReport(int id) throws SQLException {
        return reports.get(id);
    }



}
