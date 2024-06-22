package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public interface CompoundTrackPart extends Track {
    Frame getStartFrame();
    Frame getEndFrame();
}
