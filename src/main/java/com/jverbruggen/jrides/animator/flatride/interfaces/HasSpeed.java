package com.jverbruggen.jrides.animator.flatride.interfaces;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;

public interface HasSpeed extends FlatRideComponent {
    FlatRideComponentSpeed getFlatRideComponentSpeed();
}
