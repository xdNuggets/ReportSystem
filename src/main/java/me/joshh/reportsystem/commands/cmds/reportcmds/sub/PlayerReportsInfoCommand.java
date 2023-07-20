package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.menus.impl.PlayerReportsInfoMenu;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerReportsInfoCommand extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "Shows all reports made on a certain player. Clicking on a report shows extra information.";
    }

    @Override
    public String getSyntax() {
        return "/reports player <username>";
    }

    @Override
    public void perform(Player p, String[] args){
        try {
            ArrayList<Report> reports = new ArrayList<>();
            PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reported=?");
            ps.setString(1, Bukkit.getPlayer(args[1]).getUniqueId().toString());
            ps.executeQuery();
            while(ps.getResultSet().next()) {
                reports.add(new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reporter"))), ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"), ps.getResultSet().getString("id")));
            }
            new PlayerReportsInfoMenu(ReportSystem.getInstance().getPlayerMenuUtility(p), reports, false).open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}


