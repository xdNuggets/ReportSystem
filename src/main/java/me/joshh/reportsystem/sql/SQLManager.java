package me.joshh.reportsystem.sql;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class SQLManager {

    private final MySQL sql = ReportSystem.sql;

    private final int id = Report.id;


    public void createReportTable() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), PRIMARY KEY (id))");
        ps.executeUpdate();

    }

    private String generateID(int length) {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

        StringBuffer randomString = new StringBuffer(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }


    public void createReport(Player reporter, Player reported, String reason, String date) throws SQLException {
        PreparedStatement ps;

        ps = sql.getConnection().prepareStatement("INSERT INTO reports (id, reporter, reported, reason, date) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, generateID(7));
        ps.setString(2, reporter.getUniqueId().toString());
        ps.setString(3, reported.getUniqueId().toString());
        ps.setString(4, reason);
        ps.setString(5, date);
        ps.executeUpdate();

    }


    public void deleteReport(int id) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public int getID(String reported) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT id FROM reports WHERE reported = ?");
        ps.setString(1, reported);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return ps.getResultSet().getInt("id");
        }else {
            return 0;
        }
    }


    public static Report getReportByReporter(String reporter) throws SQLException {
        PreparedStatement ps;
        ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reporter = ?");
        ps.setString(1, reporter);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reporter"))), ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"));
        }else {
            return null;
        }
    }


    public static Report getReportByReported(String reported) throws SQLException {
        PreparedStatement ps;
        ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reported = ?");
        ps.setString(1, Bukkit.getPlayer(reported).getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reporter"))), ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"));
        }else {
            return null;
        }
    }









}
