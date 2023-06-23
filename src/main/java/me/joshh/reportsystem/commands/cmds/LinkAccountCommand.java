package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.SessUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LinkAccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (commandSender instanceof Player ? (Player) commandSender : null);
        String token = SQLManager.generateID(7);
        if(SQLManager.isMCLinked(player)) {
            player.sendMessage("§cYour account is already linked!");
            return false;
        }
        try {
            PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO discord_linked_accounts (minecraftUUID, token, discordID, linked) VALUES (?, ?, ?, ?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, token);
            ps.setString(3, "");
            ps.setString(4, "false");
            ps.executeUpdate();
            player.sendMessage("§aYour token is: §e" + token + "§a. Please type /linkaccount " + token + " in the linkaccount channel on our discord server.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return false;
    }
}
