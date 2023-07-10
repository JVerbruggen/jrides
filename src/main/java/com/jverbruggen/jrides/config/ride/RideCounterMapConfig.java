package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.models.map.ridecounter.RideCounterMapType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;

public class RideCounterMapConfig {
    private final String rideCounterMapIdentifier;
    private final RideCounterMapType rideCounterMapType;
    private final List<Integer> lines;
    private final List<Integer> mapIds;
    private final String lineFormat;

    public RideCounterMapConfig(String rideCounterMapIdentifier, RideCounterMapType rideCounterMapType, List<Integer> lines, List<Integer> mapIds, String lineFormat) {
        this.rideCounterMapIdentifier = rideCounterMapIdentifier;
        this.rideCounterMapType = rideCounterMapType;
        this.lines = lines;
        this.mapIds = mapIds;
        this.lineFormat = lineFormat;
    }

    public RideCounterMapConfig() {
        this(null, null, null, null, null);
    }

    public String getRideCounterMapIdentifier() {
        return rideCounterMapIdentifier;
    }

    public RideCounterMapType getRideCounterMapType() {
        return rideCounterMapType;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public List<Integer> getMapIds() {
        return mapIds;
    }

    public String getLineFormat() {
        return lineFormat;
    }

    public static RideCounterMapConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RideCounterMapConfig();

        String rideCounterMapIdentifier = configurationSection.getName();
        RideCounterMapType rideCounterMapType = configurationSection.contains("type") ? RideCounterMapType.valueOf(Objects.requireNonNull(configurationSection.getString("type")).toUpperCase()) : null;
        List<Integer> lines = configurationSection.contains("lines") ? configurationSection.getIntegerList("lines") : null;
        List<Integer> mapIds = configurationSection.contains("mapIds") ? configurationSection.getIntegerList("mapIds") : null;
        String lineFormat = configurationSection.contains("lineFormat") ? configurationSection.getString("lineFormat") : null;

        return new RideCounterMapConfig(rideCounterMapIdentifier, rideCounterMapType, lines, mapIds, lineFormat);
    }
}
