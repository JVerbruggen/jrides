package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class CoasterHandle implements RideHandle {
    private Ride ride;
    private RideControlMenu rideControlMenu;
    private RideController rideController;
    private Track track;
    private World world;
    private ParticleTrackVisualisationTool visualisationTool;
    private List<StationHandle> stationHandles;
    private List<TrainHandle> trains;
    private EffectTriggerCollection effectTriggerCollection;
    private final String dispatchSound;
    private final String restraintOpenSound;
    private final String restraintCloseSound;
    private final String windSound;

    public CoasterHandle(Ride ride, World world, String dispatchSound, String restraintOpenSound, String restraintCloseSound, String windSound) {
        this.ride = ride;
        this.world = world;
        this.dispatchSound = dispatchSound;
        this.restraintOpenSound = restraintOpenSound;
        this.restraintCloseSound = restraintCloseSound;
        this.windSound = windSound;
        this.rideController = null;

        this.trains = new ArrayList<>();
        this.stationHandles = new ArrayList<>();
        this.visualisationTool = null;
        this.track = null;
        this.effectTriggerCollection = null;
    }

    @Override
    public void start(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L);
        rideController.start();
    }

    public void setTrains(List<TrainHandle> trains) {
        this.trains = trains;
        trains.forEach(t -> t.setCoasterHandle(this));
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
    public void setRideController(RideController rideController) {
        this.rideController = rideController;
        this.rideControlMenu = ServiceProvider.getSingleton(RideControlMenuFactory.class)
                .getControlMenu(rideController);
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getStationHandle(null).getTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(String contextOwner) {
        return getStationHandle(contextOwner).getTriggerContext();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return stationHandles.stream()
                .filter(s -> s.getEjectLocation() != null)
                .map(StationHandle::getEjectLocation)
                .findFirst().orElse(null);
    }

    public void addStationHandle(StationHandle stationHandle) {
        stationHandles.add(stationHandle);
    }

    public List<StationHandle> getStationHandles() {
        return stationHandles;
    }

    public StationHandle getStationHandle(String identifier){
        return getStationHandles().get(0); // TODO: actually implement this
    }

    @Override
    public RideControlMenu getRideControlMenu() {
        return rideControlMenu;
    }

    private void tick(){
        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
    }

    public EffectTriggerCollection getEffectTriggerCollection() {
        return effectTriggerCollection;
    }

    public void setEffectTriggerCollection(EffectTriggerCollection effectTriggerCollection) {
        this.effectTriggerCollection = effectTriggerCollection;
    }

    public String getDispatchSound() {
        return dispatchSound;
    }

    public String getRestraintOpenSound() {
        return restraintOpenSound;
    }

    public String getRestraintCloseSound() {
        return restraintCloseSound;
    }

    public String getWindSound() {
        return windSound;
    }

}
