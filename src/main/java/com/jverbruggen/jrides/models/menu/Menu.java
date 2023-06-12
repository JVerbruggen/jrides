package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public interface Menu {
    Map<Player, Inventory> getSessions();
    void addSession(Player player, Inventory inventory);
    void removeSession(Player player);

    Inventory getInventoryFor(Player player);
    Menu addButton(MenuButton button);
    MenuButton getButton(UUID buttonUUID);
}
