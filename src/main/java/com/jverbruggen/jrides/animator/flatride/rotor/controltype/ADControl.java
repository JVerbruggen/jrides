package com.jverbruggen.jrides.animator.flatride.rotor.controltype;

import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import org.bukkit.ChatColor;

public class ADControl implements ControlType {
    @Override
    public double processInstruction(InstructionType instruction, double data) {
        if(instruction == InstructionType.A){
            return -data;
        }else if(instruction == InstructionType.D){
            return data;
        }
        else return 0;
    }

    @Override
    public String getControlMessageTitle() {
        return ChatColor.RED + "Take Control!";
    }

    @Override
    public String getControlMessageSubtitle() {
        return ChatColor.GOLD + "Press A or D";
    }
}
