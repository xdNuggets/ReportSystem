package me.joshh.reportsystem.commands.subcommands;


import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.menus.impl.StatisticsMenu;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;



public class StatisticsCommand extends SubCommand {
    @Override
    public String getName() {
        return "statistics";
    }

    @Override
    public String getDescription() {
        return "Sends your report statistics, which includes the amount of reports you have made, how many have been accepted, and how many have been denied";
    }

    @Override
    public String getSyntax() {
        return "/rs statistics";
    }

    @Override
    public void perform(Player player, String[] args) {
        new StatisticsMenu(ReportSystem.getInstance().getPlayerMenuUtility(player)).open();
    }
}
