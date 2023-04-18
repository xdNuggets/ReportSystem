package me.joshh.reportsystem.commands.admin;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class SendReportsMap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            HashMap<String, List<Report>> reports = ReportSystem.activeReports;

            for(String key : reports.keySet()) {
                p.sendMessage("User reported: " + key);
                for(Report r : reports.get(key)) {
                    p.sendMessage("Reason:" + r.getReason());
                    p.sendMessage("Reporter: " + r.getReporter().getName());


                }
                p.sendMessage("Size: " + reports.size());
                p.sendMessage("-------");
            }


        }
        return false;
    }
}
