package me.joshh.reportsystem.discord.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.discord.Bot;
import me.joshh.reportsystem.util.SessUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.SQLException;

public class PlayerInfoCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("info")) {
            String name = event.getOption("username").getAsString();
            Player player = (Bukkit.getPlayer(name) == null ? Bukkit.getPlayer(name) : (Player) Bukkit.getOfflinePlayer(name));
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle("Player Information");
            embed.setColor(Bot.color);
            embed.setFooter("Report System | Created by Josh", null);
            embed.addField("Username", player.getName(), false);
            embed.addField("Online:", player.isOnline() ? "Yes" : "No", false);
            // TODO: Let admins add fields to this embed through the config
            embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());

            event.replyEmbeds(embed.build()).queue();

        }
    }
}
