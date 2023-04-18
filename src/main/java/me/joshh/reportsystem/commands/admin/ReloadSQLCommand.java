package me.joshh.reportsystem.commands.admin;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReloadSQLCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("rs.admin")) {
                MySQL sql = ReportSystem.sql;
                try {
                    PreparedStatement ps = sql.getConnection().prepareStatement("DELETE FROM reports");
                    ps.executeUpdate();
                    p.sendMessage("§a(!) SQL database has been reloaded.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
}
