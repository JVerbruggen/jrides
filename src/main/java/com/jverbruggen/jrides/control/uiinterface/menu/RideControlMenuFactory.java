package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.action.RunnableButtonAction;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RideControlMenuFactory {
    private final Map<Player, RideControlMenu> openRideControlMenus;

    public RideControlMenuFactory() {
        this.openRideControlMenus = new HashMap<>();
    }

    public RideControlMenu getControlMenu(RideController rideController){
        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();
        String rideIdentifier = rideController.getRide().getIdentifier();

        RideControlButton claimOperatingButton = new SimpleRideControlButton(
                rideIdentifier,
                getItemStack(Material.RED_CONCRETE_POWDER, ChatColor.RED + "Claim operating cabin"),
                4, null);

        RideDispatchButton dispatchButton = new RideDispatchButton(rideController, 10);

        RideControlButton problemList = new SimpleRideControlButton(
                rideIdentifier,
                getItemStack(Material.ITEM_FRAME, ChatColor.RED + "Problems", dispatchLockCollection.getProblems()),
                11, null);

        RideControlButton gateButton = new SimpleRideControlButton(
                rideIdentifier,
                getItemStack(Material.RED_CONCRETE, ChatColor.RED + "Gates"),
                15, null);
        RideControlButton restraintButton = new SimpleRideControlButton(
                rideIdentifier,
                getItemStack(Material.RED_CONCRETE, ChatColor.RED + "Restraints"),
                16, new RunnableButtonAction(p -> rideController.getTriggerContext().getRestraintTrigger().execute(p)));

        dispatchLockCollection.addEventListener(lock -> {
            List<String> problems = dispatchLockCollection.getProblems();
            if(problems.size() == 0){
                problemList.setVisible(false);
                dispatchButton.setReadyForDispatch();
            }else{
                problemList.setVisible(true);
                problemList.changeLore(problems);
                dispatchButton.setNoDispatch();
            }

            problemList.sendUpdate();
        });

        RideControlMenu rideControlMenu = new RideControlMenu(rideController);
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(gateButton);
        rideControlMenu.addButton(restraintButton);

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
