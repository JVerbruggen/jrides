package com.jverbruggen.jrides.control.uiinterface.menu.event;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.control.uiinterface.menu.SimpleRideControlButton;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ButtonClickEventListener implements Listener {
    private final RideControlMenuFactory rideControlMenuFactory;
    private final RideManager rideManager;
    private final PlayerManager playerManager;

    public ButtonClickEventListener() {
        this.rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if(event.getView().getTopInventory() != clickedInventory) return;

        Player player = playerManager.getPlayer((org.bukkit.entity.Player) event.getWhoClicked());
        if(!rideControlMenuFactory.hasOpenRideControlMenu(player)) return;
        if(rideControlMenuFactory.getOpenRideControlMenu(player).getSessions().get(player) != clickedInventory) return;

        ItemStack item = event.getCurrentItem();

        if(item == null || item.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(item);

        if(!nbtItem.hasTag(SimpleRideControlButton.BUTTON_RIDE_IDENTIFIER_KEY)) return;
        if(!nbtItem.hasTag(SimpleRideControlButton.BUTTON_UUID_KEY)) return;

        String buttonRideIdentifierString = nbtItem.getString(SimpleRideControlButton.BUTTON_RIDE_IDENTIFIER_KEY);
        String buttonUUIDString = nbtItem.getString(SimpleRideControlButton.BUTTON_UUID_KEY);
        UUID buttonUUID = UUID.fromString(buttonUUIDString);

        RideHandle rideHandle = rideManager.getRideHandle(buttonRideIdentifierString);
        RideControlButton button = rideHandle.getRideControlMenu().getButton(buttonUUID);
        button.press(player);

        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = playerManager.getPlayer((org.bukkit.entity.Player) event.getPlayer());
        rideControlMenuFactory.removeOpenRideControlMenu(player);
    }
}
