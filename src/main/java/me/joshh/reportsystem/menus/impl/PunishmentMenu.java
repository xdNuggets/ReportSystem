package me.joshh.reportsystem.menus.impl;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.menus.Menu;
import me.joshh.reportsystem.menus.PlayerMenuUtility;
import me.joshh.reportsystem.sql.SQLManager;
import me.joshh.reportsystem.util.Notification;
import me.joshh.reportsystem.util.DiscordAlertManager;
import me.joshh.reportsystem.util.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;

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

    private SQLManager manager = ReportSystem.getInstance().getSQLManager();

    @Override
    public void handleMenu(InventoryClickEvent e) throws SQLException {
        String reportedUser = e.getInventory().getTitle().replace("§7", "").replace("'s Report", "");
        Report report;
        try {
            report = manager.getReportByReported(reportedUser);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        assert report != null;
        DiscordAlertManager discordAlertManager = ReportSystem.discordAlertManager;
        switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§7Warn":
                String warnReason = changeReason((Player) e.getWhoClicked());
                // Warn stuff
                manager.acceptReport(report, warnReason, ((Player) e.getWhoClicked()));
            case "§8Mute":
                String muteReason = changeReason((Player) e.getWhoClicked());
                // Mute stuff
                manager.acceptReport(report, muteReason, ((Player) e.getWhoClicked()));


            case "§cKick":
                String kickReason = changeReason((Player) e.getWhoClicked());
                // Kick stuff
                manager.acceptReport(report, kickReason, ((Player) e.getWhoClicked()));
            case "§4Ban":
                String banReason = changeReason((Player) e.getWhoClicked());
                //TODO: allow config to change ban command
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + reportedUser + " " + banReason);
                manager.acceptReport(report, banReason, ((Player) e.getWhoClicked()));
        }
        discordAlertManager.sendAcceptedReportNotification(report, ((Player) e.getWhoClicked()));
        ReportSystem.getInstance().getNotificationManager().addNotification(new Notification((Player)e.getWhoClicked(), report, "PENDING", "ACCEPTED"));
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
