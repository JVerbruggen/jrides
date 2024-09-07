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

package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.menu.*;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.controller.ButtonUpdateController;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.lore.LoreSet;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LockResembledControlButton extends BaseRideControlButton implements MenuButton {
    private final MenuButton parentButton;
    private boolean buttonOkState;
    private final ButtonVisual buttonBlockedVisual;
    private final ButtonVisual buttonOkVisual;
    private final DispatchLock lock;

    public LockResembledControlButton(ButtonVisual buttonBlockedVisual, ButtonVisual buttonOkVisual, int slot, DispatchLock lock, MenuButtonAction action) {
        this.buttonBlockedVisual = buttonBlockedVisual;
        this.buttonOkVisual = buttonOkVisual;
        this.lock = lock;
        this.buttonOkState = isLockOk();

        this.parentButton = new SimpleMenuButton(
                getActiveVisual(), slot, action);

        this.lock.addEventListener(this::onLockEvent);
        ServiceProvider.getSingleton(ButtonUpdateController.class).addButton(this, 10);
    }

    private void onLockEvent(DispatchLock lock){
        updateState();
    }

    private boolean isLockOk(){
        return lock.isUnlocked();
    }

    public void sendUpdate(){
        parentButton.sendUpdate();
    }

    @Override
    public void forceUpdate() {
        parentButton.forceUpdate();
    }

    public void changeDisplayName(String displayName){
        parentButton.changeDisplayName(displayName);
    }

    public void changeMaterial(Material material){
        parentButton.changeMaterial(material);
    }

    public void changeTitleColor(ChatColor color){
        parentButton.changeTitleColor(color);
    }

    public void changeLore(LoreSet loreSet){
        parentButton.changeLore(loreSet);
    }

    public ItemStack getItemStack(JRidesPlayer player){
        return parentButton.getItemStack(player);
    }

    public void setItemStack(ItemStack itemStack) {
        parentButton.setItemStack(itemStack);
    }

    public void setVisible(boolean visible){
        parentButton.setVisible(visible);
    }

    public void setParentMenu(Menu parentMenu) {
        parentButton.setParentMenu(parentMenu);
    }

    public Menu getParentMenu() {
        return parentButton.getParentMenu();
    }

    public UUID getUuid() {
        return parentButton.getUuid();
    }

    public int getSlot() {
        return parentButton.getSlot();
    }

    public void press(Player player){
        parentButton.press(player);
        updateState();
    }

    private void updateState(){
        buttonOkState = isLockOk();
        setButtonVisual(getActiveVisual());
        updateVisual();
        forceUpdate();
        sendUpdate();
    }

    @Override
    public ButtonVisual getActiveVisual() {
        if(buttonOkState){
            return buttonOkVisual;
        }else return buttonBlockedVisual;
    }

    @Override
    public void setActiveVisual(ButtonVisual buttonVisual) {

    }

    @Override
    public void updateVisual() {
        ButtonVisual buttonVisual = getActiveVisual();
        buttonVisual.clearUpdate();

        parentButton.setActiveVisual(buttonVisual);
//        forceUpdate();
//        sendUpdate();
    }
}
