package me.joshh.reportsystem.discord.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.util.SessUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;

public class ReportStatsCmd extends ListenerAdapter {


    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("stats"));
        SessUser user;
        try {
            user = SessUser.getSessUser(event.getUser().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(!user.isLinked()) {
            event.reply("You are not linked to a minecraft account!").setEphemeral(true).queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Report Statistics");
        embed.setDescription("Here are your current report statistics!");

        embed.addField("Total Reports", "`0`", false);
        embed.addField("Total Reports Accepted", "`0`", false);
        embed.addField("Total Reports Denied", "`0`", false);
        embed.addField("Total Reports Pending", "`0`", false);
        embed.setColor(Color.getColor(ReportSystem.config.getString("discord.embed-color")));
        embed.setFooter("Report System | Created by Josh", null);
        //TODO: Add thumbnail

        event.replyEmbeds(embed.build()).queue();

    }
}
