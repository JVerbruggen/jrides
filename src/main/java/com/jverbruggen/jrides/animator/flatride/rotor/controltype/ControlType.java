package com.jverbruggen.jrides.animator.flatride.rotor.controltype;

import com.jverbruggen.jrides.models.ride.seat.InstructionType;

public interface ControlType {
    double processInstruction(InstructionType instruction, double data);
    String getControlMessageTitle();
    String getControlMessageSubtitle();
}
