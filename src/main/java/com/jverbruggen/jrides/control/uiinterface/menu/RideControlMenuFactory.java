package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.action.RunnableButtonAction;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class RideControlMenuFactory {
    private final Map<Player, RideControlMenu> openRideControlMenus;

    public RideControlMenuFactory() {
        this.openRideControlMenus = new HashMap<>();
    }

    public RideControlMenu getControlMenu(RideController rideController){
        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();

        RideControlButton dispatchButton = new RideControlButton(
                rideController.getRide().getIdentifier(),
                getItemStack(Material.RED_CONCRETE, ChatColor.RED + "Dispatch"),
                0,
                new RunnableButtonAction(p -> {
                    DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
                    dispatchTrigger.dispatch();
                    p.sendMessage(ChatColor.GOLD + "Dispatched");
                }));

        RideControlButton problemList = new RideControlButton(
                rideController.getRide().getIdentifier(),
                getItemStack(Material.ITEM_FRAME, ChatColor.RED + "Problems", dispatchLockCollection.getProblems()),
                8,
                new RunnableButtonAction(p -> {}));

        Consumer<DispatchLock> consumer = lock -> {
            List<String> problems = dispatchLockCollection.getProblems();
            if(problems.size() == 0){
                problemList.setVisible(false);
            }else{
                problemList.setVisible(true);
                problemList.changeLore(problems);
            }

            problemList.sendUpdate();
        };
        dispatchLockCollection.addLockEventListener(consumer);
        dispatchLockCollection.addUnlockEventListener(consumer);

        RideControlMenu rideControlMenu = new RideControlMenu(rideController);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);

        return rideControlMenu;
    }

    public void addOpenRideControlMenu(Player player, RideControlMenu rideControlMenu, Inventory inventory){
        rideControlMenu.addSession(player, inventory);
        openRideControlMenus.put(player, rideControlMenu);
    }

    public RideControlMenu getOpenRideControlMenu(Player player){
        return openRideControlMenus.get(player);
    }

    public void removeOpenRideControlMenu(Player player){
        RideControlMenu rideControlMenu = openRideControlMenus.get(player);
        if(rideControlMenu == null) return;

        rideControlMenu.removeSession(player);
        openRideControlMenus.remove(player);
    }

    public boolean hasOpenRideControlMenu(Player player){
        return openRideControlMenus.containsKey(player);
    }

    private ItemStack getItemStack(Material material, String displayName){
        return ItemStackFactory.getRideControlButtonStack(material, displayName);
    }

    private ItemStack getItemStack(Material material, String displayName, List<String> lore){
        return ItemStackFactory.getRideControlButtonStack(material, displayName, lore);
    }
}
