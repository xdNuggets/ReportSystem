package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.commands.SubCommand;
import org.bukkit.entity.Player;

public class PlayerActiveReportsCommand extends SubCommand {

    @Override
    public String getName() {
        return "reports ";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

    }
}
