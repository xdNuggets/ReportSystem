package me.joshh.reportsystem.discord.cmds.staff;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;

public class ReportInfoCmd extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println(ReportSystem.config.getString("discord-bot.staff-role-id"));
        Role role = event.getGuild().getRoleById(ReportSystem.config.getString("discord-bot.staff-role-id"));
        if(event.getMember().getRoles().contains(role)) {
            if(event.getName().equals("reportinfo")) {
                Report report;
                try {
                    report = ReportSystem.getInstance().getSQLManager().getReportWithID(event.getOption("report-id").getAsString());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                assert report != null;
                EmbedBuilder embed = new EmbedBuilder();
                // Can't add this yet cuz i havent implemented the check for statuses
                // Color color = (report.getStatus().equals("Accepted")) ? Color.GREEN : Color.RED;
                embed.setTitle("Report Information");
                embed.setDescription("Report ID: `" + report.getID() + "`");

                embed.addField("Reported User", report.getReportedUser().getName(), false);
                embed.addField("Reported By", report.getReporter().getName(), false);
                embed.addField("Reason", report.getReason(), false);
                //embed.addField("Status", report.getStatus(), false);
                embed.addField("Reported at", report.getDate(), false);
                embed.setFooter("Report System | Created by Josh", null);

                event.replyEmbeds(embed.build()).queue();
            }
        } else {
            event.reply("You do not have permission to use this command!").setEphemeral(true).queue();
        }
    }
}
