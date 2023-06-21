package com.jverbruggen.jrides.common;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MenuSessionManager {
    private final Map<JRidesPlayer, Menu> openMenus;

    public MenuSessionManager(){
        this.openMenus = new HashMap<>();
    }

    public void addOpenMenu(JRidesPlayer player, Menu menu, Inventory inventory){
        menu.addSession(player, inventory);
        openMenus.put(player, menu);
        menu.sendUpdate();
    }

    public Menu getOpenMenu(JRidesPlayer player){
        return openMenus.get(player);
    }

    public void removeOpenMenu(JRidesPlayer player){
        Menu menu = openMenus.get(player);
        if(menu == null) return;

        menu.removeSession(player);
        openMenus.remove(player);
    }

    public boolean hasOpenMenu(JRidesPlayer player){
        return openMenus.containsKey(player);
    }

    public void closeAllOpenMenus(){
        for(Map.Entry<JRidesPlayer, Menu> entry : openMenus.entrySet()){
            JRidesPlayer player = entry.getKey();
            Player bukkitPlayer = player.getBukkitPlayer();
            bukkitPlayer.closeInventory();
            removeOpenMenu(player);
        }
    }
}
