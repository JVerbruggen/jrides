/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.items;

import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemStackFactory {
    public static ItemStack getCoasterStack(Material material, Integer damageValue, boolean unbreakable) {
        return new ItemStackBuilder(material)
                .setDisplayName(ChatColor.GOLD + "jrides model")
                .setDamage(damageValue)
                .setUnbreakable(unbreakable)
                .build();
    }

    public static ItemStack getCoasterStack(Material material, int customModelData){
        return new ItemStackBuilder(material)
                .setDisplayName(ChatColor.GOLD + "jrides model")
                .setCustomModelData(customModelData)
                .build();
    }

    public static ItemStack getCoasterStackFromConfig(ItemStackConfig itemStackConfig){
        int customModelData = itemStackConfig.getCustomModelData();
        if(customModelData != -1){
            return getCoasterStack(itemStackConfig.getMaterial(), customModelData);
        }

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
