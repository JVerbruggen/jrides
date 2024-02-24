package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.models.ride.seat.AbstractSeat;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

public class FlatRideSeat extends AbstractSeat {
    private PlayerControl playerControl;

    public FlatRideSeat(FlatRideHandle parentRideHandle, SeatHost seatHost, VirtualEntity virtualEntity, Vector3PlusYaw offset) {
        super(parentRideHandle, seatHost, virtualEntity, offset);
        this.playerControl = null;

        virtualEntity.setHostSeat(this);
        setRestraint(parentRideHandle.getFirstTriggerContext().getRestraintTrigger().getLock().isUnlocked());
    }

    public void setPlayerControl(PlayerControl playerControl) {
        this.playerControl = playerControl;
    }

    @Override
    public boolean supportsPlayerControl() {
        return playerControl != null;
    }

    @Override
    public void sendPlayerControlInstruction(InstructionType instruction) {
        if(playerControl == null) return;

        this.playerControl.processInstructionAsync(instruction);
    }

    @Override
    protected void onPassengerEnter(Player passenger) {
        super.onPassengerEnter(passenger);

        if(playerControl != null)
            playerControl.addControlling(passenger);
    }

    @Override
    protected void onPassengerExit(Player passenger) {
        super.onPassengerExit(passenger);

        if(playerControl != null)
            playerControl.removeControlling(passenger);
    }
}
