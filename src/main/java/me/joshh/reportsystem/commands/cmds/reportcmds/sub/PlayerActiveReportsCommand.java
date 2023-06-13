package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.commands.SubCommand;
import org.bukkit.entity.Player;

public class PlayerActiveReportsCommand extends SubCommand {

    @Override
    public String getName() {
        return "preports";
    }

    @Override
    public String getDescription() {
        return "Shows a player all the reports they have created.";
    }

    @Override
    public String getSyntax() {
        return "/preports";
    }

    @Override
    public void perform(Player player, String[] args) {

    }
}
