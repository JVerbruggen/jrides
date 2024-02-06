package com.jverbruggen.jrides.animator.flatride.interfaces;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;

public interface HasPosition extends HasSpeed {
    void setInstructionPosition(double position);

    double getInstructionPosition();

    double getLowerOperatingRange();
    double getUpperOperatingRange();

    void setLowerOperatingRange(double lower);
    void setUpperOperatingRange(double upper);

    void goTowards(double targetPosition, double fromPosition, double acceleration, FlatRideComponentSpeed componentSpeed);
}
