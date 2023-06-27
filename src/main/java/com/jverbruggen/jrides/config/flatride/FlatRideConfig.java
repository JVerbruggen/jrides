package com.jverbruggen.jrides.config.flatride;

import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfig;
import com.jverbruggen.jrides.config.flatride.timing.TimingConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.config.gates.StationConfig;
import com.jverbruggen.jrides.config.ride.AbstractRideConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlatRideConfig extends AbstractRideConfig {
    private final StationConfig stationConfig;
    private final StructureConfig structureConfig;
    private final TimingConfig timingConfig;

    public FlatRideConfig(String manifestVersion, String identifier, String displayName, List<String> displayDescription, ItemConfig displayItem, PlayerLocation warpLocation, GatesConfig gates, boolean canExitDuringRide, StationConfig stationConfig, SoundsConfig soundsConfig, StructureConfig structureConfig, TimingConfig timingConfig) {
        super(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, gates, soundsConfig, canExitDuringRide);
        this.stationConfig = stationConfig;
        this.structureConfig = structureConfig;
        this.timingConfig = timingConfig;
    }

    public StructureConfig getStructureConfig() {
        return structureConfig;
    }

    public TimingConfig getTimingConfig() {
        return timingConfig;
    }

    public StationConfig getStationConfig() {
        return stationConfig;
    }

    public static FlatRideConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");

        List<String> displayDescription = Arrays.stream(getString(configurationSection, "displayDescription", "").split("\\\\n"))
                .map(d -> ChatColor.GRAY + d)
                .collect(Collectors.toList());
        if(displayDescription.size() == 1 && ChatColor.stripColor(displayDescription.get(0)).equals(""))
            displayDescription.clear();

        ItemConfig displayItem = ItemConfig.fromConfigurationSection(configurationSection.getConfigurationSection("displayItem"));
        PlayerLocation warpLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        GatesConfig gates = GatesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("gates"));
        SoundsConfig sounds = SoundsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("sounds"));
        boolean canExitDuringRide = getBoolean(configurationSection, "canExitDuringRide", false);

        StationConfig stationConfig = StationConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "station"));
        StructureConfig structureConfig = StructureConfig.fromConfigurationSection(Objects.requireNonNull(getConfigurationSection(configurationSection, "structure"), "Structure for flatride not present"));
        TimingConfig timingConfig = TimingConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "timing"));

        return new FlatRideConfig(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, gates, canExitDuringRide, stationConfig, sounds, structureConfig, timingConfig);
    }
}
