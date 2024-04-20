package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.config.coaster.objects.ControllerConfig;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.controller.DualRideController;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.*;
import com.jverbruggen.jrides.control.uiinterface.menu.button.factory.RideControlButtonFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.SimpleMenu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.*;

public class RideControlMenuFactory {
    private final MenuSessionManager menuSessionManager;
    private final LanguageFile languageFile;
    private final RideControlButtonFactory rideControlButtonFactory;

    private final Map<RideController, Menu> adminRideControlMenus;

    public RideControlMenuFactory() {
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.rideControlButtonFactory = ServiceProvider.getSingleton(RideControlButtonFactory.class);

        this.adminRideControlMenus = new HashMap<>();
    }

    public Menu getRideControlMenu(RideController rideController, ControllerConfig controllerConfig){
        String type = controllerConfig != null ? controllerConfig.getType() : null;

        if(type == null || type.equalsIgnoreCase(ControllerConfig.CONTROLLER_DEFAULT)){
            return getSimpleControlMenu(rideController);
        }else if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_ALTERNATE)){
            return getAlternateControlMenu((DualRideController) rideController);
        }else if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_SIMULTANEOUS)){
            return getSimultaneousMenu((DualRideController) rideController);
        }else
            throw new RuntimeException("Cannot create ride control menu of type " + type);
    }

    public SimpleMenu getSimpleControlMenu(RideController rideController){
        if(rideController == null || !rideController.isActive())
            return null;

        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();

        SimpleDispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
        StationTrigger gateTrigger = rideController.getTriggerContext().getGateTrigger();
        StationTrigger restraintTrigger = rideController.getTriggerContext().getRestraintTrigger();

        MenuButton claimOperatingButton  = rideControlButtonFactory.createClaimRideButton(rideController, 4);
        MenuButton dispatchButton        = rideControlButtonFactory.createDispatchButton(dispatchTrigger, 10);
        MenuButton problemList           = rideControlButtonFactory.createProblemList(dispatchLockCollection, 11);
        MenuButton gateButton            = rideControlButtonFactory.createGateButton(gateTrigger, 15);
        MenuButton restraintButton       = rideControlButtonFactory.createRestraintButton(restraintTrigger, 16);

        SimpleMenu rideControlMenu = new SimpleMenu(3, getRideControlMenuTitle(rideController));
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(gateButton);
        rideControlMenu.addButton(restraintButton);

        return rideControlMenu;
    }

    public Menu getAlternateControlMenu(DualRideController rideController){
        if(!rideController.isActive())
            return null;

        TriggerContext leftStationTriggerContext    = rideController.getLeftStationTriggerContext();
        SimpleDispatchTrigger leftDispatchTrigger         = leftStationTriggerContext.getDispatchTrigger();
        StationTrigger leftGateTrigger              = leftStationTriggerContext.getGateTrigger();
        StationTrigger leftRestraintTrigger         = leftStationTriggerContext.getRestraintTrigger();
        TriggerContext rightStationTriggerContext   = rideController.getRightStationTriggerContext();
        SimpleDispatchTrigger rightDispatchTrigger        = rightStationTriggerContext.getDispatchTrigger();
        StationTrigger rightGateTrigger             = rightStationTriggerContext.getGateTrigger();
        StationTrigger rightRestraintTrigger        = rightStationTriggerContext.getRestraintTrigger();

        MenuButton claimOperatingButton      = rideControlButtonFactory.createClaimRideButton(rideController, 4);
        MenuButton leftProblemList           = rideControlButtonFactory.createProblemList(leftStationTriggerContext.getDispatchLockCollection(), 18);
        MenuButton leftDispatchButton        = rideControlButtonFactory.createDispatchButton(leftDispatchTrigger, 9);
        MenuButton leftGateButton            = rideControlButtonFactory.createGateButton(leftGateTrigger, 10);
        MenuButton leftRestraintButton       = rideControlButtonFactory.createRestraintButton(leftRestraintTrigger, 11);
        MenuButton rightProblemList          = rideControlButtonFactory.createProblemList(rightStationTriggerContext.getDispatchLockCollection(), 24);
        MenuButton rightDispatchButton       = rideControlButtonFactory.createDispatchButton(rightDispatchTrigger, 15);
        MenuButton rightGateButton           = rideControlButtonFactory.createGateButton(rightGateTrigger, 16);
        MenuButton rightRestraintButton      = rideControlButtonFactory.createRestraintButton(rightRestraintTrigger, 17);

        Menu rideControlMenu = new SimpleMenu(3, getRideControlMenuTitle(rideController));
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(leftProblemList);
        rideControlMenu.addButton(leftDispatchButton);
        rideControlMenu.addButton(leftGateButton);
        rideControlMenu.addButton(leftRestraintButton);
        rideControlMenu.addButton(rightProblemList);
        rideControlMenu.addButton(rightDispatchButton);
        rideControlMenu.addButton(rightGateButton);
        rideControlMenu.addButton(rightRestraintButton);

        return rideControlMenu;
    }

    public Menu getSimultaneousMenu(DualRideController rideController){
        if(!rideController.isActive())
            return null;

        TriggerContext leftStationTriggerContext    = rideController.getLeftStationTriggerContext();
        SimpleDispatchTrigger leftDispatchTrigger         = leftStationTriggerContext.getDispatchTrigger();
        StationTrigger leftGateTrigger              = leftStationTriggerContext.getGateTrigger();
        StationTrigger leftRestraintTrigger         = leftStationTriggerContext.getRestraintTrigger();
        TriggerContext rightStationTriggerContext   = rideController.getRightStationTriggerContext();
        SimpleDispatchTrigger rightDispatchTrigger        = rightStationTriggerContext.getDispatchTrigger();
        StationTrigger rightGateTrigger             = rightStationTriggerContext.getGateTrigger();
        StationTrigger rightRestraintTrigger        = rightStationTriggerContext.getRestraintTrigger();

        List<DispatchTrigger> dispatchTriggers      = List.of(leftDispatchTrigger, rightDispatchTrigger);
        DispatchLockCollection hybridDispatchLockCollection = DispatchLockCollection.makeHybrid(
                "Main locks",
                leftDispatchTrigger.getDispatchLockCollection(),
                rightDispatchTrigger.getDispatchLockCollection());
        MultiDispatchTrigger simultaneousDispatchTrigger = new MultiDispatchTrigger(dispatchTriggers, hybridDispatchLockCollection);

        MenuButton claimOperatingButton      = rideControlButtonFactory.createClaimRideButton(rideController, 4);
        MenuButton dispatchButton            = rideControlButtonFactory.createDispatchButton(simultaneousDispatchTrigger, 9);
        MenuButton leftProblemList           = rideControlButtonFactory.createProblemList(leftStationTriggerContext.getDispatchLockCollection(), 20);
        MenuButton leftGateButton            = rideControlButtonFactory.createGateButton(leftGateTrigger, 11);
        MenuButton leftRestraintButton       = rideControlButtonFactory.createRestraintButton(leftRestraintTrigger, 12);
        MenuButton rightProblemList          = rideControlButtonFactory.createProblemList(rightStationTriggerContext.getDispatchLockCollection(), 23);
        MenuButton rightGateButton           = rideControlButtonFactory.createGateButton(rightGateTrigger, 14);
        MenuButton rightRestraintButton      = rideControlButtonFactory.createRestraintButton(rightRestraintTrigger, 15);

        Menu rideControlMenu = new SimpleMenu(3, getRideControlMenuTitle(rideController));
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(leftProblemList);
        rideControlMenu.addButton(leftGateButton);
        rideControlMenu.addButton(leftRestraintButton);
        rideControlMenu.addButton(rightProblemList);
        rideControlMenu.addButton(rightGateButton);
        rideControlMenu.addButton(rightRestraintButton);

        return rideControlMenu;
    }

    public Menu getAdminMenu(RideController rideController){
        if(rideController == null || !rideController.isActive())
            return null;
        if(adminRideControlMenus.containsKey(rideController))
            return adminRideControlMenus.get(rideController);

        RideHandle rideHandle = rideController.getRideHandle();

        MenuButton openRideButton        = rideControlButtonFactory.createStateOpenRideButton(rideHandle, 3);
        MenuButton closeRideButton       = rideControlButtonFactory.createStateCloseRideButton(rideHandle, 5);

        Menu adminRideControlMenu = new SimpleMenu(1, getAdminRideControlMenuTitle(rideController));
        adminRideControlMenu.addButton(openRideButton);
        adminRideControlMenu.addButton(closeRideButton);

        adminRideControlMenus.put(rideController, adminRideControlMenu);

        return adminRideControlMenu;
    }

    private String getRideControlMenuTitle(RideController rideController){
        return languageFile.get(LanguageFileField.MENU_RIDE_CONTROL_TITLE,
                b -> b.add(LanguageFileTag.rideDisplayName, rideController.getRide().getDisplayName()));
    }

    private String getAdminRideControlMenuTitle(RideController rideController){
        return languageFile.get(LanguageFileField.MENU_ADMIN_RIDE_CONTROL_TITLE,
                b -> b.add(LanguageFileTag.rideDisplayName, rideController.getRide().getDisplayName()));
    }
}
