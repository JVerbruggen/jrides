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

package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.config.gates.StationConfig;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.RideType;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

import javax.annotation.Nonnull;

public class CoasterStationConfig extends StationConfig {
    private final boolean canSpawn;
    private final double engage;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final int passThroughCount;
    private final boolean forwardsDispatch;

    public CoasterStationConfig(double engage, double driveSpeed, int passThroughCount, boolean forwardsDispatch, int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds, StationEffectsConfig stationEffectsConfig, PlayerLocation ejectLocation, boolean canSpawn, double acceleration, double deceleration) {
        super(minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, stationEffectsConfig, ejectLocation);
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.passThroughCount = passThroughCount;
        this.forwardsDispatch = forwardsDispatch;
        this.canSpawn = canSpawn;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public int getPassThroughCount() {
        return passThroughCount;
    }

    public boolean isForwardsDispatch() {
        return forwardsDispatch;
    }

    public static CoasterStationConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        boolean canSpawn = getBoolean(configurationSection, "canSpawn", true);
        double engage = getDouble(configurationSection, "engage", 0.5);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 0.1);
        double deceleration = getDouble(configurationSection, "deceleration", 0.2);
        int passThroughCount = getInt(configurationSection, "passThroughCount", 0);
        boolean forwardsDispatch = getBoolean(configurationSection, "forwardsDispatch", true);
        int minimumWaitIntervalSeconds = getInt(configurationSection, "minimumWaitIntervalSeconds", 0);
        int maximumWaitIntervalSeconds = getInt(configurationSection, "maximumWaitIntervalSeconds", 60);
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));
        PlayerLocation ejectLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("ejectLocation"));

        return new CoasterStationConfig(engage, driveSpeed, passThroughCount, forwardsDispatch, minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects, ejectLocation, canSpawn, acceleration, deceleration);
    }

    public @Nonnull CoasterStationHandle createStationHandle(String stationName, String shortStationName, TriggerContext triggerContext, CoasterHandle coasterHandle, List<Gate> gates, DispatchLock minimumWaitTimeDispatchLock){
        EffectTriggerFactory effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);

        String rideIdentifier = coasterHandle.getRide().getIdentifier();

        StationEffectsConfig stationEffectsConfig = getStationEffectsConfig();
        List<TrainEffectTriggerHandle> entryBlockingEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.COASTER, rideIdentifier, stationEffectsConfig.getEntryBlockingEffects());
        List<TrainEffectTriggerHandle> exitBlockingEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.COASTER, rideIdentifier, stationEffectsConfig.getExitBlockingEffects());
        List<TrainEffectTriggerHandle> exitEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.COASTER, rideIdentifier, stationEffectsConfig.getExitEffects());
        PlayerLocation ejectLocation = getEjectLocation();

        MinMaxWaitingTimer waitingTimer = createWaitingTimer(minimumWaitTimeDispatchLock);

        CoasterStationHandle stationHandle = new CoasterStationHandle(coasterHandle, stationName, shortStationName, triggerContext, gates, waitingTimer,
                entryBlockingEffectTriggers, exitBlockingEffectTriggers, exitEffectTriggers, ejectLocation);
        triggerContext.setParentStation(stationHandle);

        return stationHandle;
    }
}
