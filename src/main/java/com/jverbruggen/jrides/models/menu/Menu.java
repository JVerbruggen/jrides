package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public interface Menu {
    Map<JRidesPlayer, Inventory> getSessions();
    void addSession(JRidesPlayer player, Inventory inventory);
    void removeSession(JRidesPlayer player);

    Inventory getInventoryFor(JRidesPlayer player);
    Menu addButton(MenuButton button);
    MenuButton getButton(UUID buttonUUID);
    void sendUpdate();
}
