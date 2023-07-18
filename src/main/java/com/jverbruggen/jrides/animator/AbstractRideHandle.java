package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.event.ride.RideStateUpdatedEvent;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import org.bukkit.Sound;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRideHandle implements RideHandle{
    private World world;
    private Ride ride;
    private RideState rideState;
    private boolean loaded;
    private Menu rideControlMenu;
    private RideController rideController;
    private final SoundsConfig sounds;

    private List<RideCounterRecord> topRideCounters;

    public AbstractRideHandle(World world, Ride ride, RideState rideState, boolean loaded, SoundsConfig sounds) {
        this.world = world;
        this.ride = ride;
        this.rideState = rideState;
        this.loaded = loaded;
        this.rideController = null;
        this.topRideCounters = new ArrayList<>();

        this.sounds = sounds;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public Ride getRide() {
        return ride;
    }

    @Override
    public RideState getState() {
        return rideState;
    }

    @Override
    public void setState(RideState state) {
        assert state != null;

        this.rideState = state;
        RideStateUpdatedEvent.send(ride, rideState);
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


    @Override
    public void broadcastRideOpen() {
        JRidesPlugin.getLanguageFile().sendMessage(
                JRidesPlugin.getBroadcastReceiver(),
                LanguageFileField.NOTIFICATION_RIDE_STATE_OPEN,
                b -> b.add(LanguageFileTag.rideDisplayName, getRide().getDisplayName()));
    }

    @Override
    public void broadcastRideClose() {
        JRidesPlugin.getLanguageFile().sendMessage(
                JRidesPlugin.getBroadcastReceiver(),
                LanguageFileField.NOTIFICATION_RIDE_STATE_CLOSED,
                b -> b.add(LanguageFileTag.rideDisplayName, getRide().getDisplayName()));
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

        Player currentOperator = getRideController().getOperator();
        if(currentOperator != null) currentOperator.setOperating(null);
        getRideControlMenu().sendUpdate();

        if(getState().getOpenState().isClosing()){
            if(canFullyClose()){
                getState().setStateFullyClosed();
            }else return false;
        }

        return true;
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
    public Menu getRideControlMenu() {
        return rideControlMenu;
    }

    @Override
    public List<RideCounterRecord> getTopRideCounters() {
        return topRideCounters;
    }

    public SoundsConfig getSounds() {
        return sounds;
    }


    @Override
    public boolean isOpen() {
        return isLoaded() && getState().getOpenState().isOpen();
    }

    @Override
    public boolean canFullyClose() {
        return getPassengers().isEmpty();
    }
}
