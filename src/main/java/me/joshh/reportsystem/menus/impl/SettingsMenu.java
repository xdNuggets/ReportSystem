package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsMenu extends Menu {
    public SettingsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Settings";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);

        switch(e.getCurrentItem().getType()) {

            case PAPER:
                ReportSystem.messages = !ReportSystem.messages;
                p.updateInventory();
                break;

            case NOTE_BLOCK:
                ReportSystem.sounds = !ReportSystem.sounds;
                p.updateInventory();
                break;

            case WATCH:
                ReportSystem.showDate = !ReportSystem.showDate;
                p.updateInventory();
                break;

            case BOOK_AND_QUILL:
                ReportSystem.banCommand = !ReportSystem.banCommand;
                p.updateInventory();
                break;

            case STAINED_GLASS_PANE:
                break;
        }
    }

    @Override
    public void setMenuItems() {
        boolean sounds = ReportSystem.sounds;
        boolean messages = ReportSystem.messages;
        boolean showDate = ReportSystem.showDate;
        boolean banCommand = ReportSystem.banCommand;
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


        inventory.setItem(4, soundsItem);
        inventory.setItem(3, messagesItem);
        inventory.setItem(5, banCommandItem);
        inventory.setItem(6, showDateItem);
        inventory.setItem(0, borderItem);
        inventory.setItem(1, borderItem);
        inventory.setItem(2, borderItem);
        inventory.setItem(7, borderItem);
        inventory.setItem(8, borderItem);
    }
}
