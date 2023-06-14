package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.event.ride.RideStateUpdatedEvent;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import org.bukkit.Sound;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoasterHandle implements RideHandle {
    private Ride ride;
    private Menu rideControlMenu;
    private RideController rideController;
    private Track track;
    private World world;
    private ParticleTrackVisualisationTool visualisationTool;
    private List<CoasterStationHandle> stationHandles;
    private List<TrainHandle> trains;
    private List<Transfer> transfers;
    private EffectTriggerCollection effectTriggerCollection;
    private final String dispatchSound;
    private final String restraintOpenSound;
    private final String restraintCloseSound;
    private final String windSound;
    private int rideOverviewMapId;
    private List<RideCounterRecordCollection> topRideCounters;
    private RideState rideState;
    private boolean loaded;

    public CoasterHandle(Ride ride, World world, String dispatchSound, String restraintOpenSound,
                         String restraintCloseSound, String windSound, int rideOverviewMapId) {
        this.ride = ride;
        this.world = world;
        this.dispatchSound = dispatchSound;
        this.restraintOpenSound = restraintOpenSound;
        this.restraintCloseSound = restraintCloseSound;
        this.windSound = windSound;
        this.rideController = null;

        this.trains = new ArrayList<>();
        this.stationHandles = new ArrayList<>();
        this.transfers = new ArrayList<>();
        this.visualisationTool = null;
        this.track = null;
        this.effectTriggerCollection = null;
        this.rideOverviewMapId = rideOverviewMapId;
        this.topRideCounters = new ArrayList<>();
        this.rideState = null;
        this.loaded = true;
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
        this.visualisationTool = ParticleTrackVisualisationTool.fromTrack(world, track, 20, trains);
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
    public Ride getRide() {
        return ride;
    }

    @Override
    public RideController getRideController() {
        return rideController;
    }

    @Override
    public void setRideController(RideController rideController, Menu rideControlMenu) {
        this.rideController = rideController;
        this.rideControlMenu = rideControlMenu;
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

    @Override
    public Menu getRideControlMenu() {
        return rideControlMenu;
    }

    public void tick(){
        if(rideController.isActive())
            rideController.getControlMode().tick();

        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
        for(Transfer transfer : transfers){
            transfer.tick();
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
    public List<RideCounterRecordCollection> getTopRideCounters() {
        return topRideCounters;
    }

    @Override
    public List<Player> getPassengers() {
        return getTrains().stream()
                .flatMap(trainHandle -> trainHandle.getTrain().getPassengers().stream())
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void setState(RideState state) {
        assert state != null;

        this.rideState = state;
        RideStateUpdatedEvent.send(ride, rideState);
    }

    @Override
    public RideState getState() {
        return rideState;
    }

    @Override
    public boolean isOpen() {
        return isLoaded() && getState().getOpenState().isOpen();
    }

    @Override
    public void open(Player authority) {
        if(!authority.hasPermission(Permissions.ELEVATED_RIDE_OPEN_STATE_CHANGE)){
            JRidesPlugin.getLanguageFile().sendMessage(authority, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
            return;
        }

        boolean opened = getState().setStateOpened(this);
        if(opened) authority.playSound(Sound.BLOCK_FENCE_GATE_OPEN);
        else authority.playSound(Sound.UI_BUTTON_CLICK);
    }

    @Override
    public void close(Player authority) {
        if(!authority.hasPermission(Permissions.ELEVATED_RIDE_OPEN_STATE_CHANGE)){
            JRidesPlugin.getLanguageFile().sendMessage(authority, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
            return;
        }

        boolean closed = attemptClose(authority);
    }

    private boolean attemptClose(@Nullable Player authority){
        if(authority != null && !authority.hasPermission(Permissions.ELEVATED_RIDE_OPEN_STATE_CHANGE)){
            JRidesPlugin.getLanguageFile().sendMessage(authority, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
            return false;
        }

        if(getState().getOpenState().isOpen()){
            boolean closed = getState().setStateClosed(this);

            if(authority != null){
                if(!closed){
                    authority.playSound(Sound.UI_BUTTON_CLICK);
                }else{
                    authority.playSound(Sound.BLOCK_FENCE_GATE_CLOSE);
                }
            }
        }

        Player currentOperator = rideController.getOperator();
        if(currentOperator != null) currentOperator.setOperating(null);
        rideControlMenu.sendUpdate();

        if(getState().getOpenState().isClosing()){
            if(canFullyClose()){
                rideState.setStateFullyClosed();
            }else return false;
        }

        return true;
    }

    @Override
    public boolean canFullyClose() {
        return getPassengers().size() == 0 && getRideController().getOperator() == null;
    }

    @Override
    public void broadcastRideOpen() {
        JRidesPlugin.getLanguageFile().sendMessage(
                JRidesPlugin.getBroadcastReceiver(),
                LanguageFileField.NOTIFICATION_RIDE_STATE_OPEN,
                b -> b.add(LanguageFileTag.rideDisplayName, ride.getDisplayName()));
    }

    @Override
    public void broadcastRideClose() {
        JRidesPlugin.getLanguageFile().sendMessage(
                JRidesPlugin.getBroadcastReceiver(),
                LanguageFileField.NOTIFICATION_RIDE_STATE_CLOSED,
                b -> b.add(LanguageFileTag.rideDisplayName, ride.getDisplayName()));
    }

    @Override
    public void unload(boolean save) {
        if(!loaded) return;
        if(save) rideState.save();
        loaded = false;
        JRidesPlugin.getLogger().info("Unloaded ride " + getRide().getDisplayName());
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
