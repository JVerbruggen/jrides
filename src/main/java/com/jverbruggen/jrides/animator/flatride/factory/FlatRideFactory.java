package com.jverbruggen.jrides.animator.flatride.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.animator.flatride.timing.TimingSequence;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.flatride.FlatRideConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.gates.GateOwnerConfigSpec;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.SimpleDispatchLock;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.controller.RideControllerFactory;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.GateTrigger;
import com.jverbruggen.jrides.control.trigger.RestraintTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.flatride.FlatRide;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlatRideFactory {
    private final RideControllerFactory rideControllerFactory;
    private final RideControlMenuFactory rideControlMenuFactory;
    private final LanguageFile languageFile;

    public FlatRideFactory() {
        rideControllerFactory = ServiceProvider.getSingleton(RideControllerFactory.class);
        rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
        languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public RideHandle createFromConfig(String rideIdentifier, World world, RideState rideState, FlatRideConfig flatRideConfig){
        String displayName = flatRideConfig.getDisplayName();
        List<String> displayDescription = flatRideConfig.getDisplayDescription();
        ItemStack displayItem = flatRideConfig.getDisplayItem().createItemStack();
        PlayerLocation warpLocation = flatRideConfig.getWarpLocation();
        boolean canExitDuringRide = flatRideConfig.canExitDuringRide();
        String shortStationName = "station";

        DispatchLockCollection dispatchLockCollection = new DispatchLockCollection("Main locks");

        DispatchLock vehicleInStation = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT), true);
        DispatchLock minimumWaitTimeDispatchLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_WAITING_TIME), true);
        DispatchLock restraintLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED), true);
        DispatchLockCollection gatesGenericLock = new DispatchLockCollection(
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_GATES_NOT_CLOSED), dispatchLockCollection);

        TriggerContext triggerContext = new TriggerContext(
                dispatchLockCollection,
                vehicleInStation,
                new DispatchTrigger(dispatchLockCollection),
                new GateTrigger(gatesGenericLock),
                new RestraintTrigger(restraintLock));

        Optional<GateOwnerConfigSpec> optionalGateOwner = flatRideConfig.getGates().getGateOwnerSpec(shortStationName);
        List<Gate> gates = optionalGateOwner.isPresent()
                ? optionalGateOwner.get().createGates(shortStationName, world, gatesGenericLock)
                : new ArrayList<>();

        FlatRideStationHandle stationHandle = flatRideConfig.getStationConfig().createFlatRideStationHandle(
                displayName + "_" + shortStationName, shortStationName, triggerContext, gates, minimumWaitTimeDispatchLock
        );

        FlatRide flatRide = new FlatRide(rideIdentifier, displayName, displayDescription, displayItem,
                warpLocation, canExitDuringRide);
        FlatRideHandle flatRideHandle = new FlatRideHandle(world, flatRide, true, stationHandle, flatRideConfig.getSoundsConfig());

        List<FlatRideComponent> components = new ArrayList<>();

        StructureConfig structureConfig = flatRideConfig.getStructureConfig();
        for(StructureConfigItem item : structureConfig.getItems()){
            item.createAndAddTo(components, flatRideHandle);
        }

        components.stream()
                .filter(FlatRideComponent::isRoot)
                .forEach(flatRideHandle::addRootComponent);

        TimingSequence timingSequence = flatRideConfig.getTimingConfig().createTimingSequence(components);
        flatRideHandle.setTimingSequence(timingSequence);

        RideController rideController = rideControllerFactory.createFlatRideController(flatRideHandle);
        Menu rideControlMenu = rideControlMenuFactory.getRideControlMenu(rideController, null);
        flatRideHandle.setRideController(rideController, rideControlMenu);

        flatRideHandle.setState(rideState);
        return flatRideHandle;
    }
}
