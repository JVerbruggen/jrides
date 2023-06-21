package com.jverbruggen.jrides.control.uiinterface.menu.button.event;

import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.SimpleMenuButton;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ButtonClickEventListener implements Listener {
    private final MenuSessionManager menuSessionManager;
    private final PlayerManager playerManager;

    public ButtonClickEventListener() {
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if(event.getView().getTopInventory() != clickedInventory) return;

        Player player = playerManager.getPlayer((org.bukkit.entity.Player) event.getWhoClicked());
        if(!menuSessionManager.hasOpenMenu(player)) return;

        Menu menu = menuSessionManager.getOpenMenu(player);
        if(menu.getSessions().get(player) != clickedInventory) return;

        if(event.getAction() != InventoryAction.PICKUP_ALL){
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.hasTag(SimpleMenuButton.BUTTON_UUID_KEY)) return;

        String buttonUUIDString = nbtItem.getString(SimpleMenuButton.BUTTON_UUID_KEY);
        UUID buttonUUID = UUID.fromString(buttonUUIDString);

        MenuButton button = menu.getButton(buttonUUID);
        button.press(player);

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemMoveTowardsMenuClick(InventoryClickEvent event){
        onItemTowardsMenu(event, event.getClickedInventory());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemMoveTowardsMenuDrag(InventoryDragEvent event){
        onItemTowardsMenu(event, event.getInventory());
    }

    private void onItemTowardsMenu(InventoryInteractEvent event, Inventory destination){
        HumanEntity whoClicked = event.getWhoClicked();
        if(!(whoClicked instanceof org.bukkit.entity.Player))
            return;

        Player player = playerManager.getPlayer((org.bukkit.entity.Player) whoClicked);
        if(event.getView().getTopInventory() != destination)
            return;

        if(!menuSessionManager.hasOpenMenu(player)) return;

        Menu menu = menuSessionManager.getOpenMenu(player);
        if(menu.getSessions().get(player) != destination) return;

        event.setCancelled(true);
        player.getBukkitPlayer().updateInventory();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = playerManager.getPlayer((org.bukkit.entity.Player) event.getPlayer());
        menuSessionManager.removeOpenMenu(player);
    }
}
