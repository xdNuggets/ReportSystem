package me.joshh.reportsystem.commands.subcommands;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import me.joshh.reportsystem.menus.impl.SettingsMenu;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsCommand extends SubCommand {


    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return "Allows administrators to change the settings of the plugin.";
    }

    @Override
    public String getSyntax() {
        return "/reportsystem settings";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(player.hasPermission("reportsystem.admin")) {
            new SettingsMenu(ReportSystem.getPlayerMenuUtility(player)).open();

        } else {
            player.sendMessage("§c(!) You do not have permission to use this command.");
        }

    }
}
