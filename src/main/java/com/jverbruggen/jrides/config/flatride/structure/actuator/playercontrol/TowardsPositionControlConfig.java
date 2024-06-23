/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorTargetPositionPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ADControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.SpaceBarControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.WSControl;

public class TowardsPositionControlConfig implements PlayerControlConfig {
    private final double lowerPosition;
    private final double upperPosition;
    private final double accelerate;

    public TowardsPositionControlConfig(double lowerPosition, double upperPosition, double accelerate) {
        this.lowerPosition = lowerPosition;
        this.upperPosition = upperPosition;
        this.accelerate = accelerate;
    }

    @Override
    public RotorPlayerControl createADPlayerControl(){
        ControlType controlType = new ADControl();
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, controlType);
    }

    @Override
    public RotorPlayerControl createWSPlayerControl() {
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, new WSControl());
    }

    @Override
    public RotorPlayerControl createSpaceBarPlayerControl() {
        ControlType controlType = new SpaceBarControl();
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, controlType);
    }
}
