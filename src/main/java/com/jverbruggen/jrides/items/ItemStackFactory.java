package com.jverbruggen.jrides.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackFactory {
    public static ItemStack getCoasterStack(Material material, Integer damageValue) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        ((Damageable)meta).setDamage(damageValue);
        meta.setDisplayName(ChatColor.GOLD + "jrides model");
        meta.setUnbreakable(true);
        stack.setItemMeta(meta);
        return stack;
    }
}
