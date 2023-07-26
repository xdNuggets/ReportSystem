package me.joshh.reportsystem.sql;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static me.joshh.reportsystem.ReportSystem.activeReports;

public class SQLManager {

    private static final MySQL mySQL = ReportSystem.sql;
    public String generateID(int length) {
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


    /**
     * Creates the report table.
     */
    public void createReportTable() {
        PreparedStatement ps;
        try {
            ps = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), status VARCHAR(8), PRIMARY KEY (id))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Report table created successfully");

    }

    /**
     * Creates the accepted report table.
     */
    public void createAcceptedReportTable() {
        PreparedStatement ps;
        try {
            ps = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS accepted_reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), status VARCHAR(8), accepted_by VARCHAR(128), PRIMARY KEY (id))");
            ps.executeUpdate();
            System.out.println("Accepted report table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the denied report table.
     */
    public void createDeniedReportTable() {
        PreparedStatement ps;
        try {
            ps = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS denied_reports (id VARCHAR(7), reporter VARCHAR(128), reported VARCHAR(128), reason VARCHAR(255), date VARCHAR(255), status VARCHAR(8), denied_by VARCHAR(128),PRIMARY KEY (id))");
            ps.executeUpdate();
            System.out.println("Denied report table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a report to the arraylist of a reported user.
     * @param report
     */
    private void addReport(Report report) {
        Player reportedPlayer = report.getReportedUser();
        String reportedUUID = reportedPlayer.getUniqueId().toString();
        ArrayList<Report> userReports = activeReports.getOrDefault(reportedUUID, new ArrayList<>());
        userReports.add(report);
        activeReports.put(reportedUUID, userReports);
    }


    /**
     * Creates a report in the database.
     * @param report
     * @throws SQLException
     */
    public void createSQLReport(Report report) throws SQLException {
        PreparedStatement ps;

        addReport(report);
        ps = mySQL.getConnection().prepareStatement("INSERT INTO reports (id, reporter, reported, reason, date, status) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, report.getID());
        ps.setString(2, report.getReporter().getUniqueId().toString());
        ps.setString(3, report.getReportedUser().getUniqueId().toString());
        ps.setString(4, report.getReason());
        ps.setString(5, report.getDate());
        ps.setString(6, "PENDING");
        ps.executeUpdate();
    }


    /**
     * Gets the report ID by the reported user's UUID.
     * @param reported
     * @return
     * @throws SQLException
     */

    public String getIDFromReportedUser(String reported) throws SQLException {
        PreparedStatement ps;
        ps = ReportSystem.sql.getConnection().prepareStatement("SELECT id FROM reports WHERE reported = ?");
        ps.setString(1, reported);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return ps.getResultSet().getString("id");
        }else {
            return "";
        }
    }

    /**
     * Gets the report by the reported player's UUID.
     * @param reported
     * @return
     * @throws SQLException
     */

    public Report getReportByReported(String reported) throws SQLException {
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

    /**
     * Accepts the report and moves it to a seperate database.
     * @param report
     * @param reason
     */

    public void acceptReport(Report report, String reason, Player player) {
        PreparedStatement ps;
        try {
            ps = ReportSystem.sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
            ps.setString(1, report.getID());
            ps.executeUpdate();

            ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO accepted_reports (id, reporter, reported, reason, date, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, report.getID());
            ps.setString(2, report.getReporter().getUniqueId().toString());
            ps.setString(3, report.getReportedUser().getUniqueId().toString());
            ps.setString(4, reason);
            ps.setString(5, report.getDate());
            ps.setString(6, "ACCEPTED");
            ps.setString(7, player.getUniqueId().toString());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void denyReport(Report report, String reason, Player player) {
        PreparedStatement ps;

        try {

            ps = ReportSystem.sql.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
            ps.setString(1, report.getID());
            ps.executeUpdate();

            ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO denied_reports (id, reporter, reported, reason, date, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, report.getID());
            ps.setString(2, report.getReporter().getUniqueId().toString());
            ps.setString(3, report.getReportedUser().getUniqueId().toString());
            ps.setString(4, reason);
            ps.setString(5, report.getDate());
            ps.setString(6, "DENIED");
            ps.setString(7, player.getUniqueId().toString());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Gathers all reports across all databases
    // TODO: Add Status parameter to Report, then uncomment everything
    public ArrayList<Report> getReports() throws SQLException {
        PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports");
        //PreparedStatement ps1 = sql.getConnection().prepareStatement("SELECT * FROM accepted_reports");
        //PreparedStatement ps2 = sql.getConnection().prepareStatement("SELECT * FROM denied_reports");
        if (ps == null) {
            return new ArrayList<>();
        }

        ps.executeQuery();
        //ps1.executeQuery();
        //ps2.executeQuery();
        ArrayList<Report> reports = new ArrayList<>();

        while (ps.getResultSet().next()) {

            String reportedUserUUID = ps.getResultSet().getString("reported");
            String reporterUUID = ps.getResultSet().getString("reporter");
            String reason = ps.getResultSet().getString("reason");
            String date = ps.getResultSet().getString("date");
            String id = ps.getResultSet().getString("id");

            System.out.println("ID: " + id);

            reports.add(new Report(Bukkit.getPlayer(UUID.fromString(reportedUserUUID)), Bukkit.getPlayer(UUID.fromString(reporterUUID)), reason, date, id));
        }

        /*while(ps1.getResultSet().next()) {
            String reportedUserUUID = ps1.getResultSet().getString("reported");
            String reporterUUID = ps1.getResultSet().getString("reporter");
            String reason = ps1.getResultSet().getString("reason");
            String date = ps1.getResultSet().getString("date");
            String id = ps1.getResultSet().getString("id");

            System.out.println("ID: " + id);

            reports.add(new Report(Bukkit.getPlayer(UUID.fromString(reportedUserUUID)), Bukkit.getPlayer(UUID.fromString(reporterUUID)), reason, date, id));

        }

        while(ps2.getResultSet().next()) {
            String reportedUserUUID = ps2.getResultSet().getString("reported");
            String reporterUUID = ps2.getResultSet().getString("reporter");
            String reason = ps2.getResultSet().getString("reason");
            String date = ps2.getResultSet().getString("date");
            String id = ps2.getResultSet().getString("id");

            System.out.println("ID: " + id);

            reports.add(new Report(Bukkit.getPlayer(UUID.fromString(reportedUserUUID)), Bukkit.getPlayer(UUID.fromString(reporterUUID)), reason, date, id));

        }

         */

        return reports;
    }


    public Report getReportWithID(String id) throws SQLException {
        PreparedStatement ps;
        ps = mySQL.getConnection().prepareStatement("SELECT * FROM reports WHERE id = ?");
        ps.setString(1, id);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reporter"))), ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"), ps.getResultSet().getString("id"));
        }else {
            return null;
        }
    }



    // Discord

    public void createDiscordTable() {
        PreparedStatement ps;
        try {
            ps = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS discord_linked_accounts (discordID VARCHAR(100), minecraftUUID VARCHAR(128), token VARCHAR(10), linked BOOLEAN, PRIMARY KEY (discordID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getDiscordLinkToken(Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();
        PreparedStatement ps;
        ps = mySQL.getConnection().prepareStatement("SELECT * FROM discord_linked_accounts WHERE minecraftUUID=?");
        ps.setString(1, uuid);
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return ps.getResultSet().getString("token");
        }else {
            return null;
        }

    }

    public Player getPlayerViaToken(String token) throws SQLException {
        PreparedStatement ps;
        ps = mySQL.getConnection().prepareStatement("SELECT * FROM discord_linked_accounts WHERE token = ?");
        ps.setString(1, token);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        if(rs.next()) {
            return Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("minecraftUUID")));
        }else {
            return null;
        }
    }

    public boolean isMCLinked(Player player) {
        String uuid = player.getUniqueId().toString();
        PreparedStatement ps;
        try {
            ps = mySQL.getConnection().prepareStatement("SELECT * FROM discord_linked_accounts WHERE minecraftUUID = ?");
            ps.setString(1, uuid);
            ps.executeQuery();
            if(ps.getResultSet().next()) {
                return ps.getResultSet().getBoolean("linked");
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ArrayList<Report> getReportsOnPlayer(Player player) throws SQLException {
        ArrayList<Report> reports = new ArrayList<>();
        PreparedStatement ps;
        ps = mySQL.getConnection().prepareStatement("SELECT * FROM reports WHERE reported = ?");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            String reportedUserUUID = rs.getString("reported");
            String reporterUUID = rs.getString("reporter");
            String reason = rs.getString("reason");
            String date = rs.getString("date");
            String id = rs.getString("id");
            String status = rs.getString("status");

            reports.add(new Report(Bukkit.getPlayer(UUID.fromString(reportedUserUUID)), Bukkit.getPlayer(UUID.fromString(reporterUUID)), reason, date, id));
        }

        return reports;
    }


    public void cancelReport(String id) throws SQLException {
        PreparedStatement ps;
        ps = mySQL.getConnection().prepareStatement("DELETE FROM reports WHERE id = ?");
        ps.setString(1, id);
        ps.executeUpdate();
    }












}
