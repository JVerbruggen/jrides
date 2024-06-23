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

package com.jverbruggen.jrides.control.uiinterface.menu.button.common;

import com.jverbruggen.jrides.models.menu.ButtonVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class StaticButtonVisual implements ButtonVisual {
    private final ChatColor buttonDisplayNameColor;
    private final String value;
    private final List<String> lore;
    private final ItemStack itemStack;

    public StaticButtonVisual(Material material, ChatColor buttonDisplayNameColor, String value) {
        this.itemStack = new ItemStack(material);
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = List.of();
    }

    public StaticButtonVisual(ItemStack itemStack, ChatColor buttonDisplayNameColor, String value) {
        this.itemStack = itemStack;
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = List.of();
    }

    public StaticButtonVisual(Material material, ChatColor buttonDisplayNameColor, String value, List<String> lore) {
        this.itemStack = new ItemStack(material);
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = lore;
    }

    public StaticButtonVisual(ItemStack itemStack, ChatColor buttonDisplayNameColor, String value, List<String> lore) {
        this.itemStack = itemStack;
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = lore;
    }

    public ChatColor getButtonDisplayNameColor() {
        return buttonDisplayNameColor;
    }

    public Material getButtonMaterial() {
        return itemStack.getType();
    }

    @Override
    public String getValue() {
        return value;
    }

    public ItemStack toItemStack(){
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(buttonDisplayNameColor + value);
        if(lore.size() != 0) itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean hasUpdate() {
        return false;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void clearUpdate() {

    }

    @Override
    public boolean needsFullItemStackReload() {
        return true;
    }
}
