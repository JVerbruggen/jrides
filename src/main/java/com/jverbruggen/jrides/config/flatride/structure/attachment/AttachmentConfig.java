package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;

import java.util.List;

public interface AttachmentConfig<T extends BaseConfig> {
    void createWithAttachment(T config, List<FlatRideComponent> components, RideHandle rideHandle);
}
