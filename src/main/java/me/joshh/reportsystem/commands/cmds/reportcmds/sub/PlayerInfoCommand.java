package me.joshh.reportsystem.commands.cmds.reportcmds.sub;

import me.joshh.reportsystem.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;

public class PlayerInfoCommand extends SubCommand {


    @Override
    public String getName() {
        return "playerinfo";
    }

    @Override
    public String getDescription() {
        return "Shows information about a player, including reports. Shows more detailed information if an administrator uses this command";
    }

    @Override
    public String getSyntax() {
        return "/playerinfo <username>";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        Player p = Bukkit.getPlayer(args[1]);

        Inventory inv = Bukkit.createInventory(player, 45, "Player information");
        ItemStack playerInfo = new ItemStack(Material.SKULL);
        SkullMeta meta = (SkullMeta) playerInfo.getItemMeta();
        meta.setOwner(p.getName());
        // TODO: Set other things like if theyre banned, alts, etc :)

        if(player.hasPermission("rs.admin")) {

        }

        else if(player.hasPermission("rs.manage")) {

        }
        else {
            player.sendMessage("§c(!) You can't run this command!");
        }
    }
}
