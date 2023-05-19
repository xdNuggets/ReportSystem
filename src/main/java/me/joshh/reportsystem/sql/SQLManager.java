package me.joshh.reportsystem.sql;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static me.joshh.reportsystem.ReportSystem.activeReports;

public class SQLManager {

    private final MySQL sql = ReportSystem.sql;
    private static String generateID(int length) {
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



    public void createReportTable() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), PRIMARY KEY (id))");
        ps.executeUpdate();
        System.out.println("Report table created successfully");

    }


    public void createAcceptedReportTable() {
        PreparedStatement ps;
        try {
            ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS accepted_reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), PRIMARY KEY (id))");
            ps.executeUpdate();
            System.out.println("Accepted report table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDeniedReportTable() {
        PreparedStatement ps;
        try {
            ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS denied_reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), PRIMARY KEY (id))");
            ps.executeUpdate();
            System.out.println("Denied report table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addReport(Player reportedPlayer, Player reporter, String reason, String date, String id) {
        if (!activeReports.containsKey(reportedPlayer.getUniqueId().toString())) {
            ArrayList<Report> reports = new ArrayList<>();
            activeReports.put(reportedPlayer.getUniqueId().toString(), reports);
            reports.add(new Report(reportedPlayer, reporter, reason, date,id ));
        } else {
            activeReports.get(reportedPlayer.getUniqueId().toString()).add(new Report(reportedPlayer, reporter, reason, date, id));
        }

    }




    public void createReport(Player reporter, Player reported, String reason, String date) throws SQLException {
        PreparedStatement ps;
        String id = generateID(7);
        addReport(reported, reported, reason, date,id);
        ps = sql.getConnection().prepareStatement("INSERT INTO reports (id, reporter, reported, reason, date) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, id);
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



    public static Report getReportByReported(String reported) throws SQLException {
        PreparedStatement ps;
        ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reported = ?");
        ps.setString(1, Bukkit.getPlayer(reported).getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reporter"))), ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"), ps.getResultSet().getString("id"));
        }else {
            return null;
        }
    }

    public static void acceptReport(Report report, String reason) {
        PreparedStatement ps;
        try {
            ps = ReportSystem.sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
            ps.setString(1, report.getID());
            ps.executeUpdate();

            ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO accepted_reports (id, reporter, reported, reason, date) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, report.getID());
            ps.setString(2, report.getReporter());
            ps.setString(3, report.getReportedUser());
            ps.setString(4, reason);
            ps.setString(5, report.getDate());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void denyReport(Report report, String reason) {
        PreparedStatement ps;

        try {

            ps = ReportSystem.sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
            ps.setString(1, report.getID());
            ps.executeUpdate();

            ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO denied_reports (id, reporter, reported, reason, date) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, report.getID());
            ps.setString(2, report.getReporter());
            ps.setString(3, report.getReportedUser());
            ps.setString(4, reason);
            ps.setString(5, report.getDate());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }









}
