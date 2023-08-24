package com.jverbruggen.jrides.animator.flatride.rotor.controltype;

import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import org.bukkit.ChatColor;

public class WSControl implements ControlType {
    @Override
    public double processInstruction(InstructionType instruction, double data) {
        if(instruction == InstructionType.W){
            return data;
        }else if(instruction == InstructionType.S){
            return -data;
        }
        else return 0;
    }

    @Override
    public String getControlMessageTitle() {
        return ChatColor.RED + "Take Control!";
    }

    @Override
    public String getControlMessageSubtitle() {
        return ChatColor.GOLD + "Press W or S";
    }
}
