package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.button.LockResembledControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.SimpleRideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonWithContextAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.BlinkingButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.CabinOccupationVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
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

        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
        StationTrigger gateTrigger = rideController.getTriggerContext().getGateTrigger();
        StationTrigger restraintTrigger = rideController.getTriggerContext().getRestraintTrigger();

        RideControlButton claimOperatingButton = new SimpleRideControlButton(
                rideIdentifier,
                new CabinOccupationVisual(rideController, new StaticButtonVisual(Material.BLACK_CONCRETE_POWDER, ChatColor.GOLD, "Claim operating cabin")),
                4, new RunnableButtonWithContextAction((p, b) -> {
                    if(p.equals(rideController.getOperator())){
                        p.setOperating(null);
                        p.sendMessage("You are no longer controlling " + rideIdentifier);
                    }else{
                        boolean set = p.setOperating(rideController);
                        if(set)
                            p.sendMessage("You are now controlling " + rideIdentifier);
                    }
        }));

        RideControlButton dispatchButton = new LockResembledControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN, "Dispatch", List.of(ChatColor.GRAY + "Not allowed")),
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.LIME_CONCRETE, ChatColor.GREEN, "Dispatch"),
                        new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN, "Dispatch")
                ),
                10, dispatchTrigger.getDispatchLockCollection(), new RunnableButtonAction(dispatchTrigger::execute));

        RideControlButton problemList = new SimpleRideControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.ITEM_FRAME, ChatColor.RED, "Problems"),
                11, null);
        problemList.changeLore(dispatchLockCollection.getProblems(1));

        RideControlButton gateButton = new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, "Gates are open"),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, "Gates are open")
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, "Gates are closed"),
                15, gateTrigger.getLock(), new RunnableButtonAction(gateTrigger::execute));
        RideControlButton restraintButton = new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, "Restraints are open"),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, "Restraints are open")
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, "Restraints are closed"),
                16, restraintTrigger.getLock(), new RunnableButtonAction(restraintTrigger::execute));

        dispatchLockCollection.addEventListener(lock -> {
            List<String> problems = dispatchLockCollection.getProblems(1);
            if(problems.size() == 0){
                problemList.setVisible(false);
            }else{
                problemList.setVisible(true);
                problemList.changeLore(problems);
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
