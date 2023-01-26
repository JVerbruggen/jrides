package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.List;

public interface TrackFactory {
    Track createSimpleTrack(CoasterHandle coasterHandle, CoasterConfig coasterConfig, List<NoLimitsExportPositionRecord> positions, int startOffset);
}
