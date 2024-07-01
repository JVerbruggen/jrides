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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStackBuilder setDamage(int damage){
        ((Damageable)this.itemMeta).setDamage(damage);
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable){
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStackBuilder setDisplayName(String displayName){
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemStackBuilder setCustomModelData(int customModelData){
        this.itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore){
        if(lore != null)
            this.itemMeta.setLore(lore);

        return this;
    }

    public ItemStack build(){
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }

}
