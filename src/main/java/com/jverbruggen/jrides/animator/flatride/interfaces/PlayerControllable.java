package com.jverbruggen.jrides.animator.flatride.interfaces;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;

public interface PlayerControllable extends FlatRideComponent {
    boolean allowsControl();
    void setAllowControl(boolean allow);
}
