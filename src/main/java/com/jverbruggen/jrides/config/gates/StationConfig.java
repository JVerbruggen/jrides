package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.StationEffectsConfig;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.RideType;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StationConfig extends BaseConfig {
    private final int minimumWaitIntervalSeconds;
    private final int maximumWaitIntervalSeconds;
    private final StationEffectsConfig stationEffectsConfig;
    private final PlayerLocation ejectLocation;

    public StationConfig(int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds, StationEffectsConfig stationEffectsConfig, PlayerLocation ejectLocation) {
        this.minimumWaitIntervalSeconds = minimumWaitIntervalSeconds;
        this.maximumWaitIntervalSeconds = maximumWaitIntervalSeconds;
        this.stationEffectsConfig = stationEffectsConfig;
        this.ejectLocation = ejectLocation;
    }

    public int getMinimumWaitIntervalSeconds() {
        return minimumWaitIntervalSeconds;
    }

    public int getMaximumWaitIntervalSeconds() {
        return maximumWaitIntervalSeconds;
    }

    public PlayerLocation getEjectLocation() {
        return ejectLocation;
    }

    public StationEffectsConfig getStationEffectsConfig() {
        return stationEffectsConfig;
    }

    public static StationConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        int minimumWaitIntervalSeconds = getInt(configurationSection, "minimumWaitIntervalSeconds", 15);
        int maximumWaitIntervalSeconds = getInt(configurationSection, "maximumWaitIntervalSeconds", 45);
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "effects"));
        PlayerLocation ejectLocation = PlayerLocation.fromDoubleList(getDoubleList(configurationSection, "ejectLocation"));

        return new StationConfig(minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects, ejectLocation);
    }

    public MinMaxWaitingTimer createWaitingTimer(DispatchLock dispatchLock){
        int minimumWaitingTime = getMinimumWaitIntervalSeconds();
        int maximumWaitingTime = getMaximumWaitIntervalSeconds();

        return new MinMaxWaitingTimer(minimumWaitingTime, maximumWaitingTime, dispatchLock);
    }

    public FlatRideStationHandle createFlatRideStationHandle(String rideIdentifier, String stationName, String shortStationName, TriggerContext triggerContext, List<Gate> gates, DispatchLock minimumWaitTimeDispatchLock){
        EffectTriggerFactory effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);

        MinMaxWaitingTimer waitingTimer = createWaitingTimer(minimumWaitTimeDispatchLock);
        List<TrainEffectTriggerHandle> entryBlockingEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.FLATRIDE, rideIdentifier, stationEffectsConfig.getEntryBlockingEffects());
        List<TrainEffectTriggerHandle> exitBlockingEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.FLATRIDE, rideIdentifier, stationEffectsConfig.getExitBlockingEffects());
        List<TrainEffectTriggerHandle> exitEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.FLATRIDE, rideIdentifier, stationEffectsConfig.getExitEffects());

        FlatRideStationHandle stationHandle = new FlatRideStationHandle(stationName, shortStationName, gates, getEjectLocation(), waitingTimer, triggerContext, entryBlockingEffectTriggers, exitBlockingEffectTriggers, exitEffectTriggers);
        triggerContext.setParentStation(stationHandle);

        return stationHandle;
    }
}
