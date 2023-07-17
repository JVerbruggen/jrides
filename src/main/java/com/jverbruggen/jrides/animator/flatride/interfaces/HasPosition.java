package com.jverbruggen.jrides.animator.flatride.interfaces;

public interface HasPosition extends HasSpeed {
    void setInstructionPosition(double position);

    double getInstructionPosition();

    double getLowerOperatingRange();
    double getUpperOperatingRange();

    void setLowerOperatingRange(double lower);
    void setUpperOperatingRange(double upper);
}
