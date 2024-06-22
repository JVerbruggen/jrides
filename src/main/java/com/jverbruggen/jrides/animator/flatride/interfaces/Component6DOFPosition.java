package com.jverbruggen.jrides.animator.flatride.interfaces;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public interface Component6DOFPosition {
    void setPositionRotation(Vector3 position, Quaternion rotation);
}
