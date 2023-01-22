package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.models.ride.Ride;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class GCRideHandle {
    private Ride ride;
    private List<NoLimitsExportPositionRecord> positions;
    private World world;
    private TrackVisualisationTool visualisationTool;

    public GCRideHandle(Ride ride, List<NoLimitsExportPositionRecord> positions, World world) {
        this.ride = ride;
        this.positions = positions;
        this.world = world;

        List<Location> locations = positions.stream()
                .map(p -> new Location(world, p.getPosX(), p.getPosY(), p.getPosZ()))
                .collect(Collectors.toList());
        this.visualisationTool = new TrackVisualisationTool(world, locations);
    }

    public TrackVisualisationTool getVisualisationTool() {
        return visualisationTool;
    }
}
