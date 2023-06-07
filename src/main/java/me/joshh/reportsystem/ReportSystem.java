package me.joshh.reportsystem;


import me.joshh.reportsystem.commands.PluginCommandManager;
import me.joshh.reportsystem.commands.admin.ReloadSQLCommand;
import me.joshh.reportsystem.commands.admin.TestingCommand;
import me.joshh.reportsystem.commands.cmds.reportcmds.ReportCommandManager;
import me.joshh.reportsystem.commands.cmds.ReportCommand;
import me.joshh.reportsystem.events.ClickEvent;
import me.joshh.reportsystem.util.Report;
import me.joshh.reportsystem.sql.MySQL;
import me.joshh.reportsystem.sql.SQLManager;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ReportSystem extends JavaPlugin {



    public static MySQL sql;
    public static FileConfiguration config;
    public static DateTimeFormatter myFormatObj;

    public static Plugin plugin;

    {
        plugin = this;
    }


    // Toggleable features
    public static boolean sounds;
    public static boolean messages;
    public static boolean banCommand;
    public static boolean showDate;
    public static HashMap<String, ArrayList<Report>> activeReports;

    public static String prefix;


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        config = getConfig();
        activeReports = new HashMap<>();



        myFormatObj = DateTimeFormatter.ofPattern(ReportSystem.config.getString("messages.date-format"));

        // Setup booleans
        sounds = getConfig().getBoolean("sounds");
        messages = getConfig().getBoolean("alert");
        banCommand = getConfig().getBoolean("use-ban-command");
        showDate = getConfig().getBoolean("show-date");

        prefix = getConfig().getString("messages.prefix").replace("X", "§");

        // Setup SQL
        sql = new MySQL();
        try {
            sql.connect();
            getLogger().info("Connected to MySQL database");

        } catch (SQLException e) {
            getLogger().info("Failed to connect to MySQL database");
        }

        if(sql.isConnected()) {

            SQLManager sqls = new SQLManager();
            try {
                sqls.createReportTable();
                sqls.createAcceptedReportTable();
                sqls.createDeniedReportTable();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Hopefully loads all active reports into the hashmap
        activeReports = getActiveReports();

        // Setup commands
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportCommandManager());
        getCommand("reportsystem").setExecutor(new PluginCommandManager());
        getCommand("sql").setExecutor(new ReloadSQLCommand());
        getCommand("test").setExecutor(new TestingCommand());


        // Setup listeners
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);





    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendSound(Player player) {
        if(sounds) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }


    public static HashMap<String, ArrayList<Report>> getActiveReports() {
        HashMap<String, ArrayList<Report>> reports = new HashMap<>();

        try {
            ArrayList<Report> allReports = SQLManager.getReports();

            for (Report report : allReports) {
                String reportedUser = report.getReportedUser();

                if (reports.containsKey(reportedUser)) {
                    reports.get(reportedUser).add(report);
                } else {
                    ArrayList<Report> userReports = new ArrayList<>();
                    userReports.add(report);
                    reports.put(reportedUser, userReports);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reports;
    }



/* Permissions

    rs.report - Allows player to use /report
    rs.admin - Allows player to manage the plugin's settings
    rs.manage - Allows player to view and manage reports
    rs.alert - Allows player to receive alerts when a report is made

 */


}
