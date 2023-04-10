package me.joshh.reportsystem.sql;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLFunctions {

    private MySQL sql = ReportSystem.sql;

    private int id = Report.id;


    public void createReportTable() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports (id INT(10), reporter VARCHAR(16), reported VARCHAR(16), reason VARCHAR(255), active VARCHAR(5), PRIMARY KEY (id))");
        ps.executeUpdate();
    }


    public void createReport(String reporter, String reported, String reason) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("INSERT INTO reports (id, reporter, reported, reason, active) VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, reporter);
        ps.setString(3, reported);
        ps.setString(4, reason);
        ps.setString(5, "true");
        ps.executeUpdate();

    }


    public void deleteReport(int id) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        Report.reports.remove(id);

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







}
