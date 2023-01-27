package com.jverbruggen.jrides.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackFactory {
    public static ItemStack getCoasterStack(Material material, Integer damageValue, boolean unbreakable) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        ((Damageable)meta).setDamage(damageValue);
        meta.setDisplayName(ChatColor.GOLD + "jrides model");
        meta.setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getRideControlButtonStack(Material material, String displayName){
        return getRideControlButtonStack(material, displayName, null, 1);
    }

    public static ItemStack getRideControlButtonStack(Material material, String displayName, List<String> lores){
        return getRideControlButtonStack(material, displayName, lores, 1);
    }

    public static ItemStack getRideControlButtonStack(Material material, String displayName, List<String> lores, int amount){
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        if(lores != null) meta.setLore(lores);
        stack.setItemMeta(meta);
        return stack;
    }
}
