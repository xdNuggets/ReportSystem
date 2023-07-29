package me.joshh.reportsystem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ReasonItem extends ItemStack {
    public String reason;
    public ItemStack item;
    public ReasonItem(Material m, String name, String reason) {
        this.reason = reason;
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" +name);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7Choose this item to set the report reason to: §e" + reason);
        meta.setLore(lore);
        item.setItemMeta(meta);
        this.item = item;

    }


    public ItemStack getItem() {
        return item;
    }
}
