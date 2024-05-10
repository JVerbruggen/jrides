package com.jverbruggen.jrides.animator.coaster;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.coaster.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.ride.RideCounterMapConfigs;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoasterHandle extends AbstractRideHandle {
    private Track track;
    private ParticleTrackVisualisationTool visualisationTool;
    private List<CoasterStationHandle> stationHandles;
    private List<TrainHandle> trains;
    private final List<Transfer> transfers;
    private EffectTriggerCollection<TrainEffectTriggerHandle> effectTriggerCollection;

    private final double dragConstant;
    private final double gravityConstant;
    private int rideOverviewMapId;
    private final RideCounterMapConfigs rideCounterMapConfigs;

    public CoasterHandle(Ride ride, World world, SoundsConfig sounds, int rideOverviewMapId, boolean loaded, double dragConstant, double gravityConstant, RideCounterMapConfigs rideCounterMapConfigs) {
        super(world, ride, null, loaded, sounds, rideCounterMapConfigs);
        this.dragConstant = dragConstant;
        this.gravityConstant = gravityConstant;

        this.trains = new ArrayList<>();
        this.stationHandles = new ArrayList<>();
        this.transfers = new ArrayList<>();
        this.visualisationTool = null;
        this.track = null;
        this.effectTriggerCollection = null;
        this.rideOverviewMapId = rideOverviewMapId;
        this.rideCounterMapConfigs = rideCounterMapConfigs;
    }

    public double getDragConstant() {
        return dragConstant;
    }

    public double getGravityConstant() {
        return gravityConstant;
    }

    public int getRideOverviewMapId(){
        return this.rideOverviewMapId;
    }

    public void setRideOverviewMapId(int rideOverviewMapId) {
        if(rideOverviewMapId<0) throw new RuntimeException("Cannot set ride overview map id to a negative value");
        this.rideOverviewMapId = rideOverviewMapId;
    }

    public void setTrains(List<TrainHandle> trains) {
        this.trains = trains;
        trains.forEach(t -> t.setCoasterHandle(this));
        this.visualisationTool = ParticleTrackVisualisationTool.fromTrack(getWorld(), track, 20, trains);
    }

    public List<TrainHandle> getTrains() {
        return trains;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ParticleTrackVisualisationTool getVisualisationTool() {
        return visualisationTool;
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getStationHandle(null).getTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(@Nonnull String contextOwner) {
        CoasterStationHandle stationHandle = getStationHandle(contextOwner);
        if(stationHandle == null) return null;
        return stationHandle.getTriggerContext();
    }

    @Override
    public TriggerContext getFirstTriggerContext() {
        return getStationHandles().get(0).getTriggerContext();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return stationHandles.stream()
                .filter(s -> s.getEjectLocation() != null)
                .map(StationHandle::getEjectLocation)
                .findFirst().orElse(null);
    }

    @Override
    public List<StationHandle> getStationHandles() {
        return stationHandles.stream().map(h->(StationHandle)h).collect(Collectors.toList());
    }

    public void addStationHandle(CoasterStationHandle stationHandle) {
        stationHandles.add(stationHandle);
    }

    public List<CoasterStationHandle> getCoasterStationHandles() {
        return stationHandles;
    }

    public CoasterStationHandle getStationHandle(String shortName){
        if(getStationHandles().size() == 0){
            JRidesPlugin.getLogger().severe("Looked for station with short name " + shortName + " but size was 0");
            return null;
        }

        return stationHandles.stream()
                .filter(s -> s.getShortName().equalsIgnoreCase(shortName))
                .findFirst()
                .orElseThrow(() -> {
                    String options = stationHandles.stream().map(StationHandle::getShortName).collect(Collectors.joining(", "));
                    return new RuntimeException("Station short name " + shortName + " did not exist. Existing stations: " + options);
                });
    }

    public CoasterStationHandle getStationHandle(int index){
        if(getStationHandles().size() <= index) return null;
        return getCoasterStationHandles().get(index);
    }

    public boolean hasStation(){
        return getStationHandles().size() > 0;
    }

    public void tick(){
        if(!isLoaded()) return;

        RideController rideController = getRideController();
        if(rideController.isActive())
            rideController.getControlMode().tick();

        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
        for(Transfer transfer : transfers){
            transfer.tick();
        }
    }

    public EffectTriggerCollection<TrainEffectTriggerHandle> getEffectTriggerCollection() {
        return effectTriggerCollection;
    }

    public void setEffectTriggerCollection(EffectTriggerCollection<TrainEffectTriggerHandle> effectTriggerCollection) {
        this.effectTriggerCollection = effectTriggerCollection;
    }

    public void addTransfer(Transfer transfer){
        transfers.add(transfer);
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public Track getTrack(){
        return track;
    }

    @Override
    public List<Player> getPassengers() {
        return getTrains().stream()
                .flatMap(trainHandle -> trainHandle.getTrain().getPassengers().stream())
                .toList();
    }

}
