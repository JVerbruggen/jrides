package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class CoasterHandle implements RideHandle {
    private Ride ride;
    private RideController rideController;
    private Track track;
    private World world;
    private ParticleTrackVisualisationTool visualisationTool;
    private List<StationHandle> stationHandles;
    private List<TrainHandle> trains;


    public CoasterHandle(Ride ride, RideController rideController, World world) {
        this.ride = ride;
        this.rideController = rideController;
        this.world = world;

        this.trains = new ArrayList<>();
        this.stationHandles = new ArrayList<>();
        this.visualisationTool = null;
        this.track = null;
    }

    @Override
    public void start(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L);
    }

    public void setTrains(List<TrainHandle> trains) {
        this.trains = trains;
        this.visualisationTool = ParticleTrackVisualisationTool.fromTrack(world, track, 20);
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ParticleTrackVisualisationTool getVisualisationTool() {
        return visualisationTool;
    }

    @Override
    public Ride getRide() {
        return ride;
    }

    @Override
    public RideController getRideController() {
        return rideController;
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getStationHandles().get(0).getDispatchTrigger();
    }

    public void addStationHandle(StationHandle stationHandle) {
        stationHandles.add(stationHandle);
    }

    public List<StationHandle> getStationHandles() {
        return stationHandles;
    }

    private void tick(){
        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
    }

}
