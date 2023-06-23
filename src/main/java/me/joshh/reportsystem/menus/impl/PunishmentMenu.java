package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.ParseException;

import static me.joshh.reportsystem.ReportSystem.changeReason;

public class PunishmentMenu extends Menu {
    Player p;
    public PunishmentMenu(PlayerMenuUtility playerMenuUtility, Player p) {
        super(playerMenuUtility);
        this.p = p;
    }

    @Override
    public String getMenuName() {
        return p.getName() + "'s Report";
    }

    @Override
    public int getSlots() {
        return 27;
    }


    @Override
    public void handleMenu(InventoryClickEvent e) {
        String reportedUser = e.getInventory().getTitle().replace("§7", "").replace("'s Report", "");
        Report report;
        try {
            report = SQLManager.getReportByReported(reportedUser);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§7Warn":
                String warnReason = changeReason((Player) e.getWhoClicked());
                // Warn stuff
                SQLManager.acceptReport(report, warnReason);

            case "§8Mute":

                String muteReason = changeReason((Player) e.getWhoClicked());
                // Mute stuff
                SQLManager.acceptReport(report, muteReason);

            case "§cKick":
                String kickReason = changeReason((Player) e.getWhoClicked());
                // Kick stuff
                SQLManager.acceptReport(report, kickReason);

            case "§4Ban":
                String banReason = changeReason((Player) e.getWhoClicked());
                // Ban stuff
                SQLManager.acceptReport(report, banReason);
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        String reportedUser = getInventory().getTitle().replace("§7", "").replace("'s Report", "");
        ItemStack warnItem = new ItemStack(Material.PAPER);
        ItemStack muteItem = new ItemStack(Material.BOOK_AND_QUILL);
        ItemStack kickItem = new ItemStack(Material.RABBIT_FOOT);
        ItemStack banItem = new ItemStack(Material.BARRIER);

        ItemMeta warnMeta = warnItem.getItemMeta();
        ItemMeta muteMeta = muteItem.getItemMeta();
        ItemMeta kickMeta = kickItem.getItemMeta();
        ItemMeta banMeta = banItem.getItemMeta();

        warnMeta.setDisplayName("§7Warn " + reportedUser);
        muteMeta.setDisplayName("§8Mute " + reportedUser);
        kickMeta.setDisplayName("§cKick " + reportedUser);
        banMeta.setDisplayName("§4Ban " + reportedUser);

        warnItem.setItemMeta(warnMeta);
        muteItem.setItemMeta(muteMeta);
        kickItem.setItemMeta(kickMeta);
        banItem.setItemMeta(banMeta);

        inventory.setItem(10, warnItem);
        inventory.setItem(12, muteItem);
        inventory.setItem(14, kickItem);
        inventory.setItem(16, banItem);

        for (int i = 0; i == inventory.getSize() - 4; i++) {
            inventory.setItem(inventory.firstEmpty(), borderItem);
        }
    }
}
