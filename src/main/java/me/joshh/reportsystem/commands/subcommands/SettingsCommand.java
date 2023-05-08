package me.joshh.reportsystem.commands.subcommands;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsCommand extends SubCommand {

    boolean sounds = ReportSystem.sounds;
    boolean messages = ReportSystem.messages;
    boolean showDate = ReportSystem.showDate;
    boolean banCommand = ReportSystem.banCommand;
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
            Inventory inv = Bukkit.createInventory(player, 9, "Report System Settings");

            ItemStack soundsItem = new ItemStack(Material.NOTE_BLOCK);
            ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
            ItemStack messagesItem = new ItemStack(Material.PAPER);
            ItemStack banCommandItem = new ItemStack(Material.BOOK_AND_QUILL);
            ItemStack showDateItem = new ItemStack(Material.WATCH);

            ItemMeta borderMeta = borderItem.getItemMeta();
            borderMeta.setDisplayName("§7");
            borderItem.setItemMeta(borderMeta);

            ItemMeta soundsMeta = soundsItem.getItemMeta();

            soundsMeta.setDisplayName("§ePlay Sounds: " + (sounds ? "§aEnabled" : "§cDisabled"));
            soundsItem.setItemMeta(soundsMeta);

            ItemMeta messagesMeta = messagesItem.getItemMeta();

            messagesMeta.setDisplayName("§eShow Alerts: " + (messages ? "§aEnabled" : "§cDisabled"));
            messagesItem.setItemMeta(messagesMeta);

            ItemMeta banCommandMeta = banCommandItem.getItemMeta();

            banCommandMeta.setDisplayName("§eBan Player: " + (banCommand ? "§aEnabled" : "§cDisabled"));
            banCommandItem.setItemMeta(banCommandMeta);

            ItemMeta showDateMeta = showDateItem.getItemMeta();

            showDateMeta.setDisplayName("§eShow Date: " + (showDate ? "§aEnabled" : "§cDisabled"));
            showDateItem.setItemMeta(showDateMeta);


            inv.setItem(4, soundsItem);
            inv.setItem(3, messagesItem);
            inv.setItem(5, banCommandItem);
            inv.setItem(6, showDateItem);
            inv.setItem(0, borderItem);
            inv.setItem(1, borderItem);
            inv.setItem(2, borderItem);
            inv.setItem(7, borderItem);
            inv.setItem(8, borderItem);

            player.openInventory(inv);

        } else {
            player.sendMessage("§c(!) You do not have permission to use this command.");
        }

    }
}
