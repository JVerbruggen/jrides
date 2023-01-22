package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;

public class ArmorstandSeat implements Seat {
    private Player passenger;
    private VirtualArmorstand virtualArmorstand;

    public ArmorstandSeat(VirtualArmorstand virtualArmorstand) {
        this.virtualArmorstand = virtualArmorstand;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public void setPassenger(Player player) {

    }
}
