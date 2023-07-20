package me.joshh.reportsystem.discord.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.SessUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkAccountCmd extends ListenerAdapter {

    private ReportSystem plugin = ReportSystem.getInstance();
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("linkaccount")) {
            SessUser user;
            try {
                user = SessUser.getSessUser(event.getUser().getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(user.isLinked()) {
                event.reply("Your account is already linked!").setEphemeral(true).queue();

            } else {
                try {
                    String token = event.getOption("token").getAsString();
                    Player player = plugin.getSQLManager().getPlayerViaToken(token);
                    assert player != null;
                    if(isMCAccountLinked(player)) {
                        event.reply("This account is already linked to another discord account!").setEphemeral(true).queue();
                        return;
                    }

                    if(player.isOnline() && !isMCAccountLinked(player)) {
                        if (token.equals(plugin.getSQLManager().getDiscordLinkToken(player))) {
                            user.linkAccount(event.getUser(), player);
                            event.reply("Your account has been linked!").setEphemeral(true).queue();
                        } else {
                            event.reply("Invalid token!").setEphemeral(true).queue();
                        }
                    }


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    private boolean isMCAccountLinked(Player player) throws SQLException {
        System.out.println(player.getName());
        PreparedStatement ps = plugin.getSQL().getConnection().prepareStatement("SELECT * FROM discord_linked_accounts WHERE minecraftUUID=?");
        ps.setString(1, player.getUniqueId().toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("linked");
        }
        return false;
    }
}
