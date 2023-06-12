package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseMenu implements Menu {
    private final HashMap<UUID, MenuButton> buttons;
    private final Map<Player, Inventory> sessions;
    private final int inventoryRows;
    private final String inventoryTitle;

    public BaseMenu(int inventoryRows, String inventoryTitle) {
        this.inventoryRows = inventoryRows;
        this.inventoryTitle = inventoryTitle;
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
}
