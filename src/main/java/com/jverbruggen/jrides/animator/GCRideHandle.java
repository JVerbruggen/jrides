package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class GCRideHandle {
    private Ride ride;
    private Track track;
    private List<TrainHandle> trains;
    private World world;
    private ParticleTrackVisualisationTool visualisationTool;

    public GCRideHandle(Ride ride, List<TrainHandle> trains, Track track, World world) {
        this.ride = ride;
        this.track = track;
        this.trains = trains;
        this.world = world;
        this.visualisationTool = ParticleTrackVisualisationTool.fromTrack(world, track, 20);

        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1l, 1l);
    }

    public ParticleTrackVisualisationTool getVisualisationTool() {
        return visualisationTool;
    }

    public void tick(){
        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
    }
}
