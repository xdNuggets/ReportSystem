package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.menus.impl.PlayerReportsInfoMenu;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerActiveReportsCommand extends SubCommand {

    @Override
    public String getName() {
        return "all";
    }

    @Override
    public String getDescription() {
        return "Shows a player all the reports they have created.";
    }

    @Override
    public String getSyntax() {
        return "/reports all";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        ArrayList<Report> reports = new ArrayList<>();
        PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM reports WHERE reporter=?");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        while(ps.getResultSet().next()) {
            reports.add(new Report(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("reported"))), player, ps.getResultSet().getString("reason"), ps.getResultSet().getString("date"), ps.getResultSet().getString("id")));
        }
        new PlayerReportsInfoMenu(ReportSystem.getInstance().getPlayerMenuUtility(player), reports, true).open();
    }
}
