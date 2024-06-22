package com.jverbruggen.jrides.animator.flatride.rotor.controltype;

import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import org.bukkit.ChatColor;

public class SpaceBarControl implements ControlType {
    @Override
    public double processInstruction(InstructionType instruction, double data) {
        if(instruction == InstructionType.SPACE){
            return data;
        }
        else return -data;
    }

    @Override
    public String getControlMessageTitle() {
        return ChatColor.RED + "Take Control!";
    }

    @Override
    public String getControlMessageSubtitle() {
        return ChatColor.GOLD + "Press Space";
    }
}
