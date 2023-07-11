package com.jverbruggen.jrides.models.ride.seat;

import com.jverbruggen.jrides.models.entity.Player;

public record PlayerControlInstruction(InstructionType instructionType, Player player) {
}
