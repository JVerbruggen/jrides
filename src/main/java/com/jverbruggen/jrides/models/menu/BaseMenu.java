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
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BaseMenu implements Menu {
    private final HashMap<UUID, MenuButton> buttons;
    private final Map<JRidesPlayer, Inventory> sessions;
    private final int inventoryRows;
    private final String inventoryTitle;

    public BaseMenu(int inventoryRows, String inventoryTitle) {
        this.inventoryRows = inventoryRows;
        this.inventoryTitle = inventoryTitle;
        this.buttons = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public Map<JRidesPlayer, Inventory> getSessions() {
        return sessions;
    }

    public void addSession(JRidesPlayer player, Inventory inventory){
        sessions.put(player, inventory);
    }

    public void removeSession(JRidesPlayer player){
        sessions.remove(player);
    }

    public Inventory getInventoryFor(JRidesPlayer player){
        Inventory inventory = Bukkit.createInventory(player.getBukkitPlayer(), 9*inventoryRows, inventoryTitle);

        for(MenuButton button : buttons.values()){
            inventory.setItem(button.getSlot(), button.getItemStack());
        }

        return inventory;
    }

    @Override
    public boolean matchesInventory(InventoryView inventoryView) {
        return inventoryView.getTitle().equalsIgnoreCase(inventoryTitle);
    }

    @Override
    public Menu addButton(MenuButton button) {
        buttons.put(button.getUuid(), button);
        button.setParentMenu(this);
        return this;
    }

    public MenuButton getButton(UUID buttonUUID){
        return buttons.get(buttonUUID);
    }

    @Override
    public void sendUpdate() {
        buttons.values().forEach(MenuButton::sendUpdate);
    }


}
