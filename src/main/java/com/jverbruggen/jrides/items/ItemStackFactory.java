package com.jverbruggen.jrides.items;

import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

import javax.annotation.Nullable;

public class ItemStackFactory {
    public static ItemStack getCoasterStack(Material material, Integer damageValue, boolean unbreakable) {
        return getStack(material, damageValue, unbreakable, ChatColor.GOLD + "jrides model", null);
    }

    public static ItemStack getStack(Material material, Integer damageValue, boolean unbreakable, String displayName, @Nullable List<String> lore) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        ((Damageable)meta).setDamage(damageValue);
        meta.setDisplayName(displayName);
        meta.setUnbreakable(unbreakable);
        if(lore != null) meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getCoasterStackFromConfig(ItemStackConfig itemStackConfig){
        return getCoasterStack(itemStackConfig.getMaterial(), itemStackConfig.getDamage(), itemStackConfig.isUnbreakable());
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

    public static ItemStack getPlayerHead(Player player, String displayName){
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
        skullMeta.setOwningPlayer(player.getBukkitPlayer());
        skullMeta.setDisplayName(displayName);
        stack.setItemMeta(skullMeta);
        return stack;
    }
}
