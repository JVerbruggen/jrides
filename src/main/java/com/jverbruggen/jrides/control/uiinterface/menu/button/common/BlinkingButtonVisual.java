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

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.lore.LoreSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlinkingButtonVisual implements ButtonVisual {
    private final StaticButtonVisual blinkLowVisual;
    private final StaticButtonVisual blinkHighVisual;
    private boolean blinkHigh;
    private StaticButtonVisual activeVisual;
    private boolean hasUpdate;

    public BlinkingButtonVisual(StaticButtonVisual blinkLowVisual, StaticButtonVisual blinkHighVisual) {
        this.blinkLowVisual = blinkLowVisual;
        this.blinkHighVisual = blinkHighVisual;
        this.activeVisual = blinkLowVisual;
        this.blinkHigh = false;
        this.hasUpdate = false;
    }

    private ButtonVisual updateButtonVisual(){
        this.blinkHigh = !this.blinkHigh;
        this.hasUpdate = true;

        this.activeVisual = blinkHigh ? blinkHighVisual : blinkLowVisual;
        return activeVisual;
    }

    public ChatColor getButtonDisplayNameColor() {
        return activeVisual.getButtonDisplayNameColor();
    }

    public Material getButtonMaterial() {
        return activeVisual.getButtonMaterial();
    }

    public ItemStack toItemStack(JRidesPlayer player){
        return activeVisual.toItemStack(player);
    }

    @Override
    public boolean update() {
        updateButtonVisual();
        return true;
    }

    @Override
    public boolean hasUpdate() {
        return hasUpdate;
    }

    @Override
    public List<String> getLore(JRidesPlayer player) {
        return activeVisual.getLore(player);
    }

    @Override
    public void clearUpdate() {
        hasUpdate = false;
    }

    @Override
    public boolean needsFullItemStackReload() {
        return false;
    }

    @Override
    public void changeDisplayName(String displayName) {
        blinkLowVisual.changeDisplayName(displayName);
        blinkHighVisual.changeDisplayName(displayName);
    }

    @Override
    public void changeMaterial(Material material) {
        blinkLowVisual.changeMaterial(material);
        blinkHighVisual.changeMaterial(material);
    }

    @Override
    public void changeTitleColor(ChatColor color) {
        blinkLowVisual.changeTitleColor(color);
        blinkHighVisual.changeTitleColor(color);
    }

    @Override
    public void changeLore(LoreSet loreSet) {
        blinkLowVisual.changeLore(loreSet);
        blinkHighVisual.changeLore(loreSet);
    }

    @Override
    public String getValue() {
        return activeVisual.getValue();
    }
}
