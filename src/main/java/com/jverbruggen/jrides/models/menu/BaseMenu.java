package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.api.JRidesPlayer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

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
