package me.joshh.reportsystem.util;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.discord.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.entity.Player;

import java.awt.*;

public class NotificationManager {

    /*
    * This class is responsible for sending notifications to the player when a report is created, accepted, denied, edited, or cancelled.
    * I couldn't think of another way to send notifications to the server so i decided to just hardcode a bunch of methods in this class.
     */

    private TextChannel reportChannel = Bot.jda.getTextChannelById(ReportSystem.config.getString("discord-bot.alerts-channel-id"));
    public NotificationManager() {

    }

    public void sendCreatedReportNotification(Report report) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("New Report");
        embedBuilder.setDescription("A new report has been created.");
        embedBuilder.addField("Reported User", report.getReportedUser().getName(), false);
        embedBuilder.addField("Reported By", report.getReporter().getName(), false);
        embedBuilder.addField("Reason", report.getReason(), false);
        embedBuilder.addField("Reported at:", report.getDate(), false);
        embedBuilder.setFooter("Reported at " + "<t:" +System.currentTimeMillis() + ":F>");
        embedBuilder.setColor(Color.GREEN);
        reportChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void sendAcceptedReportNotification(Report report, Player player) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Report Accepted");
        embedBuilder.setDescription("The report `" + report.getID() + "` has been accepted by " + player.getName());
        embedBuilder.addField("Reported User", report.getReportedUser().getName(), false);
        embedBuilder.addField("Reported By", report.getReporter().getName(), false);
        embedBuilder.addField("Reason", report.getReason(), false);
        embedBuilder.addField("Reported at:", report.getDate(), false);
        embedBuilder.setFooter("Accepted at " + "<t:" +System.currentTimeMillis() + ":F>");
        embedBuilder.setColor(Color.GREEN);
        reportChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void sendDeniedReportNotification(Report report, Player player) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Report Denied");
        embedBuilder.setDescription("The report `" + report.getID() + "` has been denied by " + player.getName());
        embedBuilder.addField("Reported User", report.getReportedUser().getName(), false);
        embedBuilder.addField("Reported By", report.getReporter().getName(), false);
        embedBuilder.addField("Reason", report.getReason(), false);
        embedBuilder.addField("Reported at:", report.getDate(), false);
        embedBuilder.setFooter("Denied at " + "<t:" +System.currentTimeMillis() + ":F>");
        embedBuilder.setColor(Color.RED);
        reportChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }



    public void sendEditedReasonNotification(Report report, Player player, String reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Report Edited");
        embedBuilder.setDescription("The report `" + report.getID() + "` has had the reason edited by " + player.getName());
        embedBuilder.addField("Old reason", report.getReason(), false);
        embedBuilder.addField("New reason", reason, false);
        embedBuilder.setFooter("Edited at " + "<t:" +System.currentTimeMillis() + ":F>");
        embedBuilder.setColor(Color.YELLOW);
        reportChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void sendCancelledReportNotification(Report report) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Report Cancelled");
        embedBuilder.setDescription("The report `" + report.getID() + "` has been cancelled by " + report.getReporter().getName());
        embedBuilder.setFooter("Cancelled at " + "<t:" +System.currentTimeMillis() + ":F>");
        embedBuilder.setColor(Color.BLACK);
        reportChannel.sendMessageEmbeds(embedBuilder.build()).queue();

    }




}
