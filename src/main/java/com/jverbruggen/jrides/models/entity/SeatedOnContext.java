package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.models.ride.seat.Seat;

enum SeatedOnState {
    CREATED,
    SETUP,
    RESTORED
}

public class SeatedOnContext {
    private SeatedOnState state;
    private final Seat seat;
    private final boolean wasFlying;
    private final boolean wasAllowedFlying;

    public SeatedOnContext(Seat seat, boolean wasFlying, boolean wasAllowedFlying) {
        this.state = SeatedOnState.CREATED;
        this.seat = seat;
        this.wasFlying = wasFlying;
        this.wasAllowedFlying = wasAllowedFlying;
    }

    public static SeatedOnContext create(Seat seat, Player player){
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        boolean wasFlying = bukkitPlayer.isFlying();
        boolean wasAllowedFlying = bukkitPlayer.getAllowFlight();

        return new SeatedOnContext(seat, wasFlying, wasAllowedFlying);
    }

    public void setup(Player player){
        if(state != SeatedOnState.CREATED) throw new RuntimeException("Already set up context");

        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();

        Sync.runSynced(() -> {
            bukkitPlayer.setFlying(true);
            bukkitPlayer.setAllowFlight(true);

            player.setSeatedOnContext(this);
            state = SeatedOnState.SETUP;
        });
    }

    public void restore(Player player){
        if(state != SeatedOnState.SETUP) throw new RuntimeException("Already restored context");

        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();

        Sync.runSynced(() -> {
            bukkitPlayer.setFlying(wasFlying);
            bukkitPlayer.setAllowFlight(wasAllowedFlying);

            player.clearSmoothAnimationRotation();

            player.setSeatedOnContext(null);
            state = SeatedOnState.RESTORED;
        });
    }

    public Seat getSeat() {
        return seat;
    }
}
