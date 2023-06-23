package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.ItemBuilder;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.UUID;

public class ReportInfoCommand extends SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Gives you information on a specific report. Contains more information than /reports";
    }

    @Override
    public String getSyntax() {
        return "/reports info <report id>";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException, ParseException {
        Inventory inv = Bukkit.createInventory(player, 27, "Report Info");

        // Setup Items
        ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        glass.setName(" ");
        ItemStack glassItem = glass.toItemStack();

        ItemBuilder reportInfo = new ItemBuilder(Material.PAPER, 1, (byte) 0);
        reportInfo.setName("Report: " + args[1]);
        reportInfo.addLoreLine(" ");
        Report report = SQLManager.getReportWithID(args[1]);
        Player reportedPlayer = report.getReportedUser();
        Player reporter = report.getReporter();

        reportInfo.addLoreLine(ReportSystem.prefix + " §eReported User: §a" + reportedPlayer.getName());
        reportInfo.addLoreLine(ReportSystem.prefix + " §eReported By: §a" + reporter.getName());
        reportInfo.addLoreLine(ReportSystem.prefix + " §eReason: §a" + report.getReason());
        reportInfo.addLoreLine(ReportSystem.prefix + " §eCreated at: §a" + report.getDate() + " §e; §8Created " + report.getTimeSinceCreation() + " ago.");
        // reportInfo.addLoreLine("Status: " + report.getStatus()); TODO: Add xd

        ItemStack reportInfoItem = reportInfo.toItemStack();

        // Add Items to Inventory
        for(ItemStack i : inv.getContents()) {
            if(i == null) {
                inv.setItem(inv.firstEmpty(), glassItem);
            }

        }

        inv.setItem(13, reportInfoItem);

        player.openInventory(inv);
    }
}
