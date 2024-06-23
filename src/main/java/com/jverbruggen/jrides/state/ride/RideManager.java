/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.factory.FlatRideFactory;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.flatride.FlatRideConfig;
import com.jverbruggen.jrides.config.ride.RidesConfig;
import com.jverbruggen.jrides.config.ride.RidesConfigObject;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.exception.CoasterLoadException;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.coaster.CoasterLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RideManager {
    private final JRidesLogger logger;
    private final List<RideHandle> rideHandles;
    private final ConfigManager configManager;
    private final CoasterLoader coasterLoader;
    private final FlatRideFactory flatRideFactory;
    private final List<String> rideIdentifiers;

    public RideManager() {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.rideHandles = new ArrayList<>();
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.coasterLoader = ServiceProvider.getSingleton(CoasterLoader.class);
        this.flatRideFactory = ServiceProvider.getSingleton(FlatRideFactory.class);
        this.rideIdentifiers = new ArrayList<>();
    }

    public void addRideHandle(RideHandle rideHandle){
        rideHandles.add(rideHandle);
    }

    public List<String> getRideIdentifiers() {
        return rideIdentifiers;
    }

    public List<RideHandle> getRideHandles() {
        return rideHandles;
    }

    public @Nullable RideHandle getRideHandle(String identifier){
        return this.rideHandles
                .stream()
                .filter(ch -> ch.getRide().getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    public void initAllRides(World world){
        RidesConfig ridesConfig = configManager.getRideConfig();
        if(ridesConfig == null) return;

        List<RidesConfigObject> ridesConfigObjects = ridesConfig.getRides();

        for (RidesConfigObject ridesConfigObject : ridesConfigObjects) {
            String rideIdentifier = ridesConfigObject.getIdentifier();
            String rideType = ridesConfigObject.getType();

            if(rideIdentifiers.contains(rideIdentifier)) throw new RuntimeException("Ride " + rideIdentifier + " identifier already exists!");
            rideIdentifiers.add(rideIdentifier);

            logger.info("Initialising ride " + rideIdentifier + " with type " + rideType);

            if(rideType.equals("coaster")) {
                try {
                    loadCoaster(world, rideIdentifier);
                } catch (CoasterLoadException e) {
                    JRidesPlugin.getLogger().severe("Could not load coaster " + rideIdentifier);
                }
            }else if(rideType.equals("flatride")){
                loadFlatRide(world, rideIdentifier);
            }else throw new RuntimeException("Ride type unknown: " + rideType);
        }

        start();
        RideInitializedEvent.send();
    }

    public void unloadAllRides(){
        getRideHandles().forEach(r -> r.unload(true));
    }

    private void loadCoaster(World world, String rideIdentifier) throws CoasterLoadException {
        RideState rideState = RideState.loadCoasterState(rideIdentifier);
        if(!rideState.shouldLoadRide()){
            logger.warning("Not loading ride " + rideIdentifier);
            return;
        }

        CoasterConfig coasterConfig = configManager.getCoasterConfig(rideIdentifier);
        if(coasterConfig == null) {
            logger.warning("Coaster '" + rideIdentifier + "' has no config file present, not loading");
            return;
        }

        CoasterHandle rideHandle = coasterLoader.loadCoaster(coasterConfig, rideIdentifier, rideState, world);
        if(rideHandle != null) this.addRideHandle(rideHandle);
    }

    private void loadFlatRide(World world, String rideIdentifier){
        RideState rideState = RideState.loadFlatrideState(rideIdentifier);
        if(!rideState.shouldLoadRide()){
            logger.warning("Not loading flatride " + rideIdentifier);
            return;
        }

        FlatRideConfig flatRideConfig = configManager.getFlatRideConfig(rideIdentifier);
        if(flatRideConfig == null) {
            logger.warning("Flatride '" + rideIdentifier + "' has no config file present, not loading");
            return;
        }

        RideHandle rideHandle = flatRideFactory.createFromConfig(rideIdentifier, world, rideState, flatRideConfig);
//        RideHandle rideHandle = flatRideFactory.createSimpleFlatRide(rideIdentifier, world, rideState);
        if(rideHandle != null) this.addRideHandle(rideHandle);
    }

    private void start(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L);
    }

    private void tick(){
        for(RideHandle rideHandle : rideHandles){
            rideHandle.tick();
        }

        Speed.incrementFractionalSpeedCounter();
    }
}
