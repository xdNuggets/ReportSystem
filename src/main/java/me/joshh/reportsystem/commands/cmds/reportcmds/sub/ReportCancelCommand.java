package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.util.Report;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;

public class ReportCancelCommand extends SubCommand {
    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "Cancels a report you have made.";
    }

    @Override
    public String getSyntax() {
        return "/report cancel <report id>";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException, ParseException {

        if(args.length == 2) {
            String id = args[1];

            Report report = sqlManager.getReportWithID(id);
            if(report == null) {
                player.sendMessage("§c(!) That report does not exist.");

            }

            if(report.getReporter().getUniqueId().equals(player.getUniqueId())) {
                sqlManager.cancelReport(id);
                player.sendMessage("§a(!) Successfully deleted report §e" + id + "§a.");
                ReportSystem.notificationManager.sendCancelledReportNotification(report);
            } else {
                player.sendMessage("§c(!) You cannot delete a report that you did not make.");
            }

        }

        if(args.length == 1) {
            player.sendMessage("§c(!) Please specify a report ID.");
        }

    }
}
