package me.joshh.reportsystem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReasonItem extends ItemStack {
    public String reason;
    public ItemStack item;
    public ReasonItem(Material m, String name, String reason) {
        this.reason = reason;
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" +name);
        meta.getLore().add("§7Choose this item to set the report reason to: §e" + reason);
        item.setItemMeta(meta);
        this.item = item;

    }


    public ItemStack getItem() {
        return item;
    }
}
