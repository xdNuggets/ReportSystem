package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        assert player != null;
        if(SQLManager.isMCLinked(player)) {
            player.sendMessage("§cYour account is already linked!");
            return false;
        }
        try {
            PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("INSERT INTO discord_linked_accounts (minecraftUUID, token, discordID, linked) VALUES (?, ?, ?, ?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, token);
            ps.setString(3, "");
            ps.setBoolean(4, false);
            ps.executeUpdate();
            TextComponent message = new TextComponent("§aYour token is: §e" + token + "§a. Please type /linkaccount " + token + " in the linkaccount channel on our discord server.");
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {new TextComponent("§aClick to copy the command! Use this in our discord server!")}));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/linkaccount token:" + token));
            player.spigot().sendMessage(message);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return false;
    }
}
