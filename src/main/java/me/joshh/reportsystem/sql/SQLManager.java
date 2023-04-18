package me.joshh.reportsystem.sql;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLManager {

    private MySQL sql = ReportSystem.sql;

    private int id = Report.id;


    public void createReportTable() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports (id INT(10), reporter VARCHAR(16), reported VARCHAR(16), reason VARCHAR(255), date VARCHAR(255), PRIMARY KEY (id))");
        ps.executeUpdate();
    }


    public void createReport(String reporter, String reported, String reason, String date) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("INSERT INTO reports (id, reporter, reported, reason, date) VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, reporter);
        ps.setString(3, reported);
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
            return new Report(Bukkit.getPlayer(ps.getResultSet().getString("reported")),  Bukkit.getPlayer(ps.getResultSet().getString("reporter")),ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"));
        }else {
            return null;
        }
    }


    public static Report getReportByReported(String reported) throws SQLException {
        PreparedStatement ps;
        ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reported = ?");
        ps.setString(1, reported);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return new Report(Bukkit.getPlayer(ps.getResultSet().getString("reported")),  Bukkit.getPlayer(ps.getResultSet().getString("reporter")),ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"));
        }else {
            return null;
        }
    }









}
