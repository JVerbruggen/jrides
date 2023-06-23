package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.rotor.Attachment;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public interface FlatRideComponent {
    String getIdentifier();
    Attachment getAttachedTo();
    void setAttachedTo(Attachment attachment);
    void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition);
    Quaternion getRotation();
    Vector3 getPosition();
    void tick();
}
