package me.joshh.reportsystem.commands.admin;

import me.joshh.reportsystem.ReportSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        try {PreparedStatement ps = ReportSystem.getInstance().getSQL().getConnection().prepareStatement("DELETE FROM reports");
        ps.executeUpdate(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
        commandSender.sendMessage("§aSuccessfully cleared all reports!");
        return false;
    }
}
