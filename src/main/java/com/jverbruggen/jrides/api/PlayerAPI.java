package com.jverbruggen.jrides.api;

import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.menu.RideOverviewMenuFactory;
import org.bukkit.inventory.Inventory;

public class PlayerAPI {
    private final PlayerManager playerManager;
    private final RideOverviewMenuFactory rideOverviewMenuFactory;
    private final MenuSessionManager menuSessionManager;

    public PlayerAPI() {
        playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        rideOverviewMenuFactory = ServiceProvider.getSingleton(RideOverviewMenuFactory.class);
        menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    public JRidesPlayer getFromBukkitPlayer(org.bukkit.entity.Player bukkitPlayer){
        return playerManager.getPlayer(bukkitPlayer);
    }

    public void displayRideOverviewMenu(JRidesPlayer player){
        Menu menu = rideOverviewMenuFactory.getRideOverviewMenu();
        Inventory inventory = menu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, menu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }
}
