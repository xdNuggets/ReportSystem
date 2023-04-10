package me.joshh.reportsystem.commands.cmds;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.functions.Report;
import me.joshh.reportsystem.sql.SQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;

public class ActiveReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            SQLFunctions sql = new SQLFunctions();
            if(player.hasPermission("reportsystem.manage")) {
                Inventory inv = Bukkit.createInventory(player, 54, "Unhandled Reports");

                ItemStack reportItem = new ItemStack(Material.BOOK);
                ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
                ItemMeta borderMeta = borderItem.getItemMeta();
                borderMeta.setDisplayName("§7");
                borderItem.setItemMeta(borderMeta);

                ReportSystem.setBorder(inv, borderItem);
                for (Report report : Report.getReports().values()) {
                    if (report.isActive()) {
                        ItemMeta meta = reportItem.getItemMeta();
                        try {
                            meta.setDisplayName("§eReport §6#" + sql.getID(report.getReported().getName()));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add("§ePlayer reported: §a" + report.getReported().getName());
                        lore.add("§eReason: §a" + report.getReason());
                        lore.add("§eReported by: §a" + report.getReporter().getName());
                        lore.add("§eReported at §a" + report.getDate());
                        meta.setLore(lore);
                        reportItem.setItemMeta(meta);
                        inv.setItem(inv.firstEmpty(), reportItem);
                    }
                }

                player.openInventory(inv);
            }else {
                player.sendMessage("§c(!) You do not have permission to use this command.");
            }
        }
        return false;
    }



}
