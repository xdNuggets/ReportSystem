package me.joshh.reportsystem.discord;

import me.joshh.reportsystem.ReportSystem;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.file.FileConfiguration;


public class Bot {

    private final FileConfiguration config = ReportSystem.config;


    JDA jda;

    public Bot(String token) {
        jda = JDABuilder.createDefault(config.getString("discord.token")).build();


        // Personalization
        Activity.ActivityType type = Activity.ActivityType.valueOf(config.getString("discord.activity-type"));
        jda.getPresence().setActivity(Activity.of(type, config.getString("discord.activity-message")));
        jda.getPresence().setStatus(OnlineStatus.fromKey(config.getString("discord.status-type")));

        // Events


        // Commands

    }
}
