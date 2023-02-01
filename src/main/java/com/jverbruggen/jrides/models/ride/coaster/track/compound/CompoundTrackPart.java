package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public interface CompoundTrackPart extends Track {
    Track getNextTrack();
    Track getPreviousTrack();
    void setNextTrack(Track track);
    void setPreviousTrack(Track track);
    Frame getStartFrame();
    Frame getEndFrame();
}
