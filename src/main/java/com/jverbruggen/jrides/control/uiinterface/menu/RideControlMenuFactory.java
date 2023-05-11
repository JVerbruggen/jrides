package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.config.coaster.objects.ControllerConfig;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.controller.AlternateRideController;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
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

    public RideControlMenu getRideControlMenu(RideController rideController, ControllerConfig controllerConfig){
        String type = controllerConfig != null ? controllerConfig.getType() : null;

        if(type == null || type.equalsIgnoreCase(ControllerConfig.CONTROLLER_DEFAULT)){
            return getSimpleControlMenu(rideController);
        }else if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_ALTERNATE)){
            return getAlternateControlMenu((AlternateRideController) rideController);
        }else
            throw new RuntimeException("Cannot create ride control menu of type " + type);
    }

    public RideControlMenu getSimpleControlMenu(RideController rideController){
        if(!rideController.isActive())
            return null;

        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();
        String rideIdentifier = rideController.getRide().getIdentifier();

        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
        StationTrigger gateTrigger = rideController.getTriggerContext().getGateTrigger();
        StationTrigger restraintTrigger = rideController.getTriggerContext().getRestraintTrigger();

        RideControlButton claimOperatingButton = rideControlButtonFactory.createClaimRideButton(rideController, rideIdentifier, 4);
        RideControlButton dispatchButton = rideControlButtonFactory.createDispatchButton(rideIdentifier, dispatchTrigger, 10);
        RideControlButton problemList = rideControlButtonFactory.createProblemList(rideIdentifier, dispatchLockCollection, 11);
        RideControlButton gateButton = rideControlButtonFactory.createGateButton(rideIdentifier, gateTrigger, 15);
        RideControlButton restraintButton = rideControlButtonFactory.createRestraintButton(rideIdentifier, restraintTrigger, 16);

        RideControlMenu rideControlMenu = new RideControlMenu();
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(gateButton);
        rideControlMenu.addButton(restraintButton);

        return rideControlMenu;
    }

    public RideControlMenu getAlternateControlMenu(AlternateRideController rideController){
        if(!rideController.isActive())
            return null;

        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();
        String rideIdentifier = rideController.getRide().getIdentifier();

        TriggerContext leftStationTriggerContext    = rideController.getLeftStationTriggerContext();
        DispatchTrigger leftDispatchTrigger         = leftStationTriggerContext.getDispatchTrigger();
        StationTrigger leftGateTrigger              = leftStationTriggerContext.getGateTrigger();
        StationTrigger leftRestraintTrigger         = leftStationTriggerContext.getRestraintTrigger();
        TriggerContext rightStationTriggerContext   = rideController.getRightStationTriggerContext();
        DispatchTrigger rightDispatchTrigger        = rightStationTriggerContext.getDispatchTrigger();
        StationTrigger rightGateTrigger             = rightStationTriggerContext.getGateTrigger();
        StationTrigger rightRestraintTrigger        = rightStationTriggerContext.getRestraintTrigger();

        RideControlButton claimOperatingButton      = rideControlButtonFactory.createClaimRideButton(rideController, rideIdentifier, 4);
        RideControlButton problemList               = rideControlButtonFactory.createProblemList(rideIdentifier, dispatchLockCollection, 6);
        RideControlButton leftDispatchButton        = rideControlButtonFactory.createDispatchButton(rideIdentifier, leftDispatchTrigger, 9);
        RideControlButton leftGateButton            = rideControlButtonFactory.createGateButton(rideIdentifier, leftGateTrigger, 10);
        RideControlButton leftRestraintButton       = rideControlButtonFactory.createRestraintButton(rideIdentifier, leftRestraintTrigger, 11);
        RideControlButton rightDispatchButton       = rideControlButtonFactory.createDispatchButton(rideIdentifier, rightDispatchTrigger, 15);
        RideControlButton rightGateButton           = rideControlButtonFactory.createGateButton(rideIdentifier, rightGateTrigger, 16);
        RideControlButton rightRestraintButton      = rideControlButtonFactory.createRestraintButton(rideIdentifier, rightRestraintTrigger, 17);

        RideControlMenu rideControlMenu = new RideControlMenu();
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(leftDispatchButton);
        rideControlMenu.addButton(leftGateButton);
        rideControlMenu.addButton(leftRestraintButton);
        rideControlMenu.addButton(rightDispatchButton);
        rideControlMenu.addButton(rightGateButton);
        rideControlMenu.addButton(rightRestraintButton);

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
}
