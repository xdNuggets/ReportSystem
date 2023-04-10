package me.joshh.reportsystem;


import me.joshh.reportsystem.commands.PluginCommandManager;
import me.joshh.reportsystem.commands.cmds.ActiveReportsCommand;
import me.joshh.reportsystem.commands.cmds.ReportCommand;
import me.joshh.reportsystem.events.ClickEvent;
import me.joshh.reportsystem.sql.MySQL;
import me.joshh.reportsystem.sql.SQLFunctions;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class ReportSystem extends JavaPlugin {



    public static MySQL sql;
    public static FileConfiguration config;


    // Toggleable features
    public static boolean sounds;
    public static boolean messages;
    public static boolean banCommand;
    public static boolean showDate;




    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        config = getConfig();

        // Setup booleans
        sounds = getConfig().getBoolean("sounds");
        messages = getConfig().getBoolean("alert");
        banCommand = getConfig().getBoolean("use-ban-command");
        showDate = getConfig().getBoolean("show-date");

        // Setup SQL
        sql = new MySQL();
        try {
            sql.connect();
            getLogger().info("Connected to MySQL database");

        } catch (SQLException e) {
            getLogger().info("Failed to connect to MySQL database");
        }

        if(sql.isConnected()) {
            getLogger().info("Successfully connected to MySQL database. Creating tables...");
            SQLFunctions sqls = new SQLFunctions();
            try {
                sqls.createReportTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Setup commands
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ActiveReportsCommand());
        getCommand("reportsystem").setExecutor(new PluginCommandManager());


        // Setup listeners
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static void setBorder(Inventory inv, ItemStack item) {
        for(int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }
        for(int i = 45; i < 54; i++) {
            inv.setItem(i, item);
        }

        for(int i = 0; i < 54; i += 9) {
            inv.setItem(i, item);
        }
        for(int i = 8; i < 54; i += 9) {
            inv.setItem(i, item);
        }

    }


    public static void sendSound(Player player) {
        if(sounds) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }



/* Permissions

    rs.report - Allows player to use /report
    rs.admin - Allows player to manage the plugin's settings
    rs.manage - Allows player to view and manage reports
    rs.alert - Allows player to receive alerts when a report is made

 */


}
