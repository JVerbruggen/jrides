package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.factory.RideControlButtonFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class RideControlMenuFactory {
    private final Map<Player, RideControlMenu> openRideControlMenus;
    private final LanguageFile languageFile;
    private final RideControlButtonFactory rideControlButtonFactory;

    public RideControlMenuFactory() {
        this.openRideControlMenus = new HashMap<>();
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.rideControlButtonFactory = ServiceProvider.getSingleton(RideControlButtonFactory.class);
    }

    public RideControlMenu getSimpleControlMenu(RideController rideController){
        if(!rideController.isActive())
            return null;

        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();
        String rideIdentifier = rideController.getRide().getIdentifier();

        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
        StationTrigger gateTrigger = rideController.getTriggerContext().getGateTrigger();
        StationTrigger restraintTrigger = rideController.getTriggerContext().getRestraintTrigger();

        RideControlButton claimOperatingButton = rideControlButtonFactory.createClaimRideButton(rideController, rideIdentifier);
        RideControlButton dispatchButton = rideControlButtonFactory.createDispatchButton(rideIdentifier, dispatchTrigger);
        RideControlButton problemList = rideControlButtonFactory.createProblemList(rideIdentifier, dispatchLockCollection);
        RideControlButton gateButton = rideControlButtonFactory.createGateButton(rideIdentifier, gateTrigger);
        RideControlButton restraintButton = rideControlButtonFactory.createRestraintButton(rideIdentifier, restraintTrigger);

        RideControlMenu rideControlMenu = new RideControlMenu();
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(gateButton);
        rideControlMenu.addButton(restraintButton);

        return rideControlMenu;
    }

    public RideControlMenu getDualControlMenu(RideController rideControllerLeft, RideController rideControllerRight){
        throw new RuntimeException("Not implemented");
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
}
