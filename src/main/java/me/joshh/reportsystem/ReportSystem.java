package me.joshh.reportsystem;


import me.joshh.reportsystem.commands.PluginCommandManager;
import me.joshh.reportsystem.commands.admin.ClearReportsCommand;
import me.joshh.reportsystem.commands.admin.ReloadSQLCommand;
import me.joshh.reportsystem.commands.admin.TestingCommand;
import me.joshh.reportsystem.commands.cmds.LinkAccountCommand;
import me.joshh.reportsystem.commands.cmds.NotificationCommand;
import me.joshh.reportsystem.commands.cmds.reportcmds.ReportCommandManager;
import me.joshh.reportsystem.commands.cmds.ReportCommand;
import me.joshh.reportsystem.discord.Bot;
import me.joshh.reportsystem.events.MenuListener;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.NotificationManager;
import me.joshh.reportsystem.util.DiscordAlertManager;
import me.joshh.reportsystem.util.Report;
import me.joshh.reportsystem.sql.MySQL;
import me.joshh.reportsystem.sql.SQLManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public final class ReportSystem extends JavaPlugin {



    public static MySQL sql;
    public static FileConfiguration config;
    public static DateTimeFormatter myFormatObj;

    public static DiscordAlertManager discordAlertManager;



    // Toggleable features
    public static boolean sounds;
    public static boolean messages;
    public static boolean banCommand;
    public static boolean showDate;
    public static HashMap<String, ArrayList<Report>> activeReports;

    public static String prefix;

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public static ReportSystem getInstance() {
        return ReportSystem.getPlugin(ReportSystem.class);
    }

    public SQLManager getSQLManager() {
        return new SQLManager();
    }

    public MySQL getSQL() {
        return sql;
    }
    private NotificationManager notificationManager;

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

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

        // dumbass prefix
        prefix = "XlX5»Xr".replace("X", "§");

        // Setup SQL
        sql = new MySQL();



        try {
            if(sql != null) {
            sql.connect();
            getLogger().info("Connected to MySQL database"); }
        } catch (SQLException e) {
            getLogger().warning(ChatColor.RED + "Failed to connect to MySQL database");
            getLogger().log(Level.SEVERE, "Disabling plugin due to error while connecting to SQL Database. Please make sure all values are correct in the config.yml");
            Bukkit.getPluginManager().disablePlugin(ReportSystem.getInstance());
        }


        if(getPlugin(ReportSystem.class).isEnabled() && sql.isConnected()) {
            SQLManager sqls = new SQLManager();
            notificationManager = new NotificationManager();
            sqls.createReportTable();
            sqls.createAcceptedReportTable();
            sqls.createDeniedReportTable();
            sqls.createDiscordTable();
            notificationManager.createTable();
            activeReports = getActiveReports();

            getCommand("report").setExecutor(new ReportCommand());
            getCommand("reports").setExecutor(new ReportCommandManager());
            getCommand("reportsystem").setExecutor(new PluginCommandManager());
            getCommand("sql").setExecutor(new ReloadSQLCommand());
            getCommand("test").setExecutor(new TestingCommand());
            getCommand("linkaccount").setExecutor(new LinkAccountCommand());
            getCommand("notifications").setExecutor(new NotificationCommand());
            getCommand("clearreports").setExecutor(new ClearReportsCommand());



            // Setup listeners
            getServer().getPluginManager().registerEvents(new MenuListener(), this);

            try {
                if(config.getString("discord-bot.token") != null) {
                new Bot(config.getString("discord-bot.token")).start(); }
                getLogger().info("Discord Bot successfully started.");
                discordAlertManager = new DiscordAlertManager();
                getLogger().info("Notification Manager successfully initialized.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }



        }


        // Discord bot



    }
    @Override
    public void onDisable() {
        if(Bot.jda != null) {
        Bot.jda.shutdownNow();}
        getLogger().info("Discord Bot successfully shutdown.");
        try {
            sql.disconnect();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "SQL never initialized. Cannot shut down.");
            throw new RuntimeException(e);
        }
        getLogger().info("Disconnected from MySQL database");
    }

    public static void sendSound(Player player) {
        if(sounds) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }


    public HashMap<String, ArrayList<Report>> getActiveReports() {
        HashMap<String, ArrayList<Report>> reports = new HashMap<>();

        try {
            ArrayList<Report> allReports = getSQLManager().getReports();

            for (Report report : allReports) {

                String reportedUser = report.getReportedUser().getUniqueId().toString();

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


    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;

        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }

    public static String changeReason(Player player) {
        String[] newReason = new String[1];
        player.sendMessage("§aPlease enter the new reason in the rename field.");
        ReportSystem.sendSound(player);
        new AnvilGUI(getInstance(), player, "Edit reason here", (p, reply) -> {
            System.out.println("hi");
            newReason[0] = reply;

            player.sendMessage("§aYou have changed the reason to: " + newReason[0]);

            return newReason[0];
        });

        return ReportSystem.config.getString("messages.default-reason");
    }




/* Permissions

    rs.report - Allows player to use /report
    rs.admin - Allows player to manage the plugin's settings
    rs.manage - Allows player to view and manage reports
    rs.alert - Allows player to receive alerts when a report is made

 */


}
