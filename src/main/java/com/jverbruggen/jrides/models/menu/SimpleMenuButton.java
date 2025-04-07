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

package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.lore.LoreSet;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class SimpleMenuButton implements MenuButton {
    private ButtonVisual buttonVisual;
    private ItemStack itemStack;
    private final int slot;
    private final UUID uuid;
    private final MenuButtonAction action;
    private Menu parentMenu;
    private boolean visible;
    private boolean hasUpdate;

    public SimpleMenuButton(ButtonVisual visual, int slot, MenuButtonAction action) {
        this.slot = slot;
        this.action = action;
        this.visible = true;
        this.hasUpdate = true;
        this.buttonVisual = visual;

        this.uuid = UUID.randomUUID();
    }

    @Override
    public void sendUpdate(){
//        if(buttonVisual.hasUpdate()){
//            setButtonVisual(buttonVisual);
//        }

        if(hasUpdate){
            hasUpdate = false;

            getParentMenu().getSessions().forEach((player, inventory) -> inventory.setItem(slot, getItemStack(player)));
        }
    }

    @Override
    public void forceUpdate() {
        hasUpdate = true;
    }

    @Override
    public void changeDisplayName(String displayName){
//        ItemStack newItemStack = itemStack.clone();
//        ItemMeta itemMeta = newItemStack.getItemMeta();
//
//        assert itemMeta != null;
//        itemMeta.setDisplayName(displayName);
//        newItemStack.setItemMeta(itemMeta);
//
//        itemStack = newItemStack;

        buttonVisual.changeDisplayName(displayName);

        forceUpdate();
        sendUpdate();
    }

    @Override
    public void changeMaterial(Material material){
//        if(itemStack.getType() == material) return;
//
//        ItemStack newItemStack = itemStack.clone();
//        newItemStack.setType(material);
//
//        itemStack = newItemStack;

        buttonVisual.changeMaterial(material);
        forceUpdate();
    }

    @Override
    public void changeTitleColor(ChatColor color){
//        String displayName = itemStack.getItemMeta().getDisplayName();
//        displayName = ChatColor.stripColor(displayName);
//        changeDisplayName(color + displayName);

        buttonVisual.changeTitleColor(color);
        forceUpdate();
    }

    @Override
    public void changeLore(LoreSet loreSet){
//        ItemStack newItemStack = itemStack.clone();
//        ItemMeta itemMeta = newItemStack.getItemMeta();
//
//        itemMeta.setLore(lore);
//        newItemStack.setItemMeta(itemMeta);
//
//        itemStack = newItemStack;

        buttonVisual.changeLore(loreSet);
        forceUpdate();
    }

    @Override
    public ItemStack getItemStack(JRidesPlayer player){
        if(!visible) return null;

        NBTItem nbtItem = new NBTItem(buttonVisual.toItemStack(player));
        nbtItem.setString(BUTTON_UUID_KEY, uuid.toString());

        return nbtItem.getItem();
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
//        NBTItem nbtItem = new NBTItem(itemStack);
//        nbtItem.setString(BUTTON_UUID_KEY, uuid.toString());

//        this.itemStack = nbtItem.getItem();

        forceUpdate();
    }

    @Override
    public void setVisible(boolean visible){
        this.visible = visible;

        forceUpdate();
    }

    @Override
    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Override
    public Menu getParentMenu() {
        return parentMenu;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void press(Player player){
        if(action == null) return;
        action.run(player, this);
        player.playSound(getPressedSound());
        forceUpdate();
        sendUpdate();
    }

    @Override
    public ButtonVisual getActiveVisual() {
        return buttonVisual;
    }

    @Override
    public void updateVisual() {
        forceUpdate();
        sendUpdate();
//        setItemStack(buttonVisual.toItemStack());
    }

    @Override
    public Sound getPressedSound() {
        return Sound.UI_BUTTON_CLICK;
    }

    @Override
    public void setActiveVisual(ButtonVisual buttonVisual) {
        this.buttonVisual = buttonVisual;
        this.buttonVisual.clearUpdate();
        forceUpdate();
        sendUpdate();
    }

    public static String BUTTON_UUID_KEY = "jrides-button-uuid";
}
