package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public interface CompoundTrackPart extends Track {
    Frame getStartFrame();
    Frame getEndFrame();
}
