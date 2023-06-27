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
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CoasterStationConfig extends StationConfig {
    private final double engage;
    private final double driveSpeed;

    public CoasterStationConfig(double engage, double driveSpeed, int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds, StationEffectsConfig stationEffectsConfig, PlayerLocation ejectLocation) {
        super(minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, stationEffectsConfig, ejectLocation);
        this.engage = engage;
        this.driveSpeed = driveSpeed;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public static CoasterStationConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = getDouble(configurationSection, "engage", 0.5);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        int minimumWaitIntervalSeconds = getInt(configurationSection, "minimumWaitIntervalSeconds", 0);
        int maximumWaitIntervalSeconds = getInt(configurationSection, "maximumWaitIntervalSeconds", 60);
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));
        PlayerLocation ejectLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("ejectLocation"));

        return new CoasterStationConfig(engage, driveSpeed, minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects, ejectLocation);
    }

    public CoasterStationHandle createStationHandle(String stationName, String shortStationName, TriggerContext triggerContext, CoasterHandle coasterHandle, List<Gate> gates, DispatchLock minimumWaitTimeDispatchLock){
        EffectTriggerFactory effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);

        String rideIdentifier = coasterHandle.getRide().getIdentifier();

        StationEffectsConfig stationEffectsConfig = getStationEffectsConfig();
        List<TrainEffectTriggerHandle> entryEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getEntryEffects());
        List<TrainEffectTriggerHandle> exitEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getExitEffects());
        PlayerLocation ejectLocation = getEjectLocation();

        MinMaxWaitingTimer waitingTimer = createWaitingTimer(minimumWaitTimeDispatchLock);

        return new CoasterStationHandle(coasterHandle, stationName, shortStationName, triggerContext, gates, waitingTimer,
                entryEffectTriggers, exitEffectTriggers, ejectLocation);
    }
}
