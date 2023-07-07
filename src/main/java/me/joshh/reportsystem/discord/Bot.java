package me.joshh.reportsystem.discord;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.discord.cmds.LinkAccountCmd;
import me.joshh.reportsystem.discord.cmds.PlayerInfoCommand;
import me.joshh.reportsystem.discord.cmds.ReportStatsCmd;
import me.joshh.reportsystem.discord.cmds.staff.ReportInfoCmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;


public class Bot {

    private final FileConfiguration config = ReportSystem.config;


    public static JDA jda;
    JDABuilder builder;
    public static Color color;


    public Bot(String token){
        builder = JDABuilder.createDefault(token);

    }

    public void start() throws InterruptedException {
        jda = builder.build().awaitReady();
        Guild guild = jda.getGuildById(config.getString("discord-bot.guild-id"));
        if(guild == null) System.out.println("No guild ID was specified. Some features may not work.");
        color = (Color.getColor(config.getString("discord-bot.embed-color")) == null ? Color.BLUE : Color.getColor(config.getString("discord-bot.embed-color")));

        // Personalization
        Activity.ActivityType type = Activity.ActivityType.valueOf(config.getString("discord-bot.activity-type"));
        jda.getPresence().setActivity(Activity.of(type, config.getString("discord-bot.activity-message")));
        jda.getPresence().setStatus(OnlineStatus.fromKey(config.getString("discord-bot.status-type")));

        // Events
        jda.addEventListener(new LinkAccountCmd());
        jda.addEventListener(new PlayerInfoCommand());
        jda.addEventListener(new ReportStatsCmd());
        jda.addEventListener(new ReportInfoCmd());


        // Commands
        assert guild != null;
        guild.upsertCommand("linkaccount", "Link your Minecraft account to your Discord account!")
                .addOption(OptionType.STRING, "token", "Your token", true)
                .queue();

        guild.upsertCommand("info", "Get information about a player!")
                .addOption(OptionType.STRING, "username", "The player's username", true)
                .queue();

        guild.upsertCommand("reportstats", "Get your report stats!").queue();

        guild.upsertCommand("reportinfo", "Get information about a report!")
                .addOption(OptionType.STRING, "report-id", "The report's ID", true)
                .queue();


    }



}
