package com.jverbruggen.jrides.config.flatride.structure.rotor.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.flatride.structure.rotor.RotorConfig;

import java.util.List;

public interface AttachmentConfig {
    void createRotorWithAttachment(RotorConfig rotorConfig, List<FlatRideComponent> components);
}
