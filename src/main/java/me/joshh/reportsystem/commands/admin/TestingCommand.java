package me.joshh.reportsystem.commands.admin;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.util.Report;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TestingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            HashMap<String, ArrayList<Report>> reportMap = ReportSystem.getActiveReports();

            System.out.println(reportMap.values().size());
            for(String s1 : reportMap.keySet()) {
                player.sendMessage("Key: " + s1);

            }

            for(ArrayList<Report> arrayList : reportMap.values()) {
                for(Report r : arrayList) {
                    player.sendMessage(r.getReason() + " ; " + r.getID());
                }
            }
        }
        return false;
    }
}
