package me.joshh.reportsystem.commands;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.sql.SQLManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;

public abstract class SubCommand {

    public SQLManager sqlManager = ReportSystem.getInstance().getSQLManager();

    //name of the subcommand ex. /prank <subcommand> <-- that
    public abstract String getName();

    //ex. "This is a subcommand that let's a shark eat someone"
    public abstract String getDescription();

    //How to use command ex. /prank freeze <player>
    public abstract String getSyntax();

    //code for the subcommand
    public abstract void perform(Player player, String args[]) throws SQLException, ParseException;

}
