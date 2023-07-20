package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.commands.SubCommand;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;

public class ReportsHelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows all subcommands for the /reports command.";
    }

    @Override
    public String getSyntax() {
        return "/reports help";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException, ParseException {

    }
}
