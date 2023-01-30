package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class RideControlMenu {
    private final RideController controller;
    private final HashMap<UUID, RideControlButton> buttons;
    private final Map<Player, Inventory> sessions;

    public RideControlMenu(RideController controller) {
        this.controller = controller;
        this.buttons = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public Map<Player, Inventory> getSessions() {
        return sessions;
    }

    public void addSession(Player player, Inventory inventory){
        sessions.put(player, inventory);
    }

    public void removeSession(Player player){
        sessions.remove(player);
    }

    public Inventory getInventoryFor(Player player){
        Inventory inventory = Bukkit.createInventory(player.getBukkitPlayer(), 9*3, "Ride control menu");

        for(RideControlButton button : buttons.values()){
            inventory.setItem(button.getSlot(), button.getItemStack());
        }

        return inventory;
    }

    public RideControlMenu addButton(RideControlButton button){
        buttons.put(button.getUuid(), button);
        button.setParentMenu(this);
        return this;
    }

    public RideControlButton getButton(UUID buttonUUID){
        return buttons.get(buttonUUID);
    }
}
