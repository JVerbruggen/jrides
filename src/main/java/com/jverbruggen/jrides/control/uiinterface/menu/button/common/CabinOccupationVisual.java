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
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.lore.LoreSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CabinOccupationVisual implements ButtonVisual {
    private final RideController rideController;
    private final ButtonVisual noOccupationVisual;
    private Player operator;
    private boolean hasUpdate;
    private final String operatedString;

    public CabinOccupationVisual(RideController rideController, ButtonVisual noOccupationVisual, String operatedString) {
        this.rideController = rideController;
        this.noOccupationVisual = noOccupationVisual;
        this.operatedString = operatedString;
        this.operator = null;
        this.hasUpdate = false;
    }

    private boolean checkForUpdate(){
        Player actualOperator = rideController.getOperator();
        if(actualOperator != null && !actualOperator.equals(operator)){
            this.operator = actualOperator;
            this.hasUpdate = true;
        }else if(actualOperator == null){
            this.operator = null;
            this.hasUpdate = true;
        }
        return hasUpdate;
    }

    private boolean isBeingOperated(){
        checkForUpdate();
        Player actualOperator = rideController.getOperator();

        return actualOperator != null;
    }

    @Override
    public ChatColor getButtonDisplayNameColor() {
        if(operator == null) return noOccupationVisual.getButtonDisplayNameColor();
        throw new RuntimeException("Should be updated through toItemStack");
    }

    @Override
    public Material getButtonMaterial() {
        if(operator == null) return noOccupationVisual.getButtonMaterial();
        throw new RuntimeException("Should be updated through toItemStack");
    }

    @Override
    public String getValue() {
        if(operator == null) return getButtonDisplayNameColor() + noOccupationVisual.getValue();
        throw new RuntimeException("Should be updated through toItemStack");
    }

    @Override
    public ItemStack toItemStack(JRidesPlayer player) {
        if(!isBeingOperated()){
            return ItemStackFactory.getRideControlButtonStack(getButtonMaterial(), getValue());
        }

        return ItemStackFactory.getPlayerHead(operator, ChatColor.GOLD + operatedString);
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean hasUpdate() {
        return checkForUpdate();
    }

    @Override
    public List<String> getLore(JRidesPlayer player) {
        return List.of();
    }

    @Override
    public void clearUpdate() {
        hasUpdate = false;
    }

    @Override
    public boolean needsFullItemStackReload() {
        return true;
    }

    @Override
    public void changeDisplayName(String displayName) {

    }

    @Override
    public void changeMaterial(Material material) {

    }

    @Override
    public void changeTitleColor(ChatColor color) {

    }

    @Override
    public void changeLore(LoreSet loreSet) {

    }
}
