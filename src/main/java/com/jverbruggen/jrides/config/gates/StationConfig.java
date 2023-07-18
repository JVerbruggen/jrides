package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.StationEffectsConfig;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.gate.Gate;
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
        int minimumWaitIntervalSeconds = getInt(configurationSection, "minimumWaitIntervalSeconds", 0);
        int maximumWaitIntervalSeconds = getInt(configurationSection, "maximumWaitIntervalSeconds", 60);
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "effects"));
        PlayerLocation ejectLocation = PlayerLocation.fromDoubleList(getDoubleList(configurationSection, "ejectLocation"));

        return new StationConfig(minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects, ejectLocation);
    }

    public MinMaxWaitingTimer createWaitingTimer(DispatchLock dispatchLock){
        int minimumWaitingTime = getMinimumWaitIntervalSeconds();
        int maximumWaitingTime = getMaximumWaitIntervalSeconds();

        return new MinMaxWaitingTimer(minimumWaitingTime, maximumWaitingTime, dispatchLock);
    }

    public FlatRideStationHandle createFlatRideStationHandle(String stationName, String shortStationName, TriggerContext triggerContext, List<Gate> gates, DispatchLock minimumWaitTimeDispatchLock){
        MinMaxWaitingTimer waitingTimer = createWaitingTimer(minimumWaitTimeDispatchLock);

        return new FlatRideStationHandle(stationName, shortStationName, gates, getEjectLocation(), waitingTimer, triggerContext);
    }
}
