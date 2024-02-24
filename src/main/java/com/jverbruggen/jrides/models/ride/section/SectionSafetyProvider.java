package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

public interface SectionSafetyProvider {
    BlockSectionSafetyResult getEnteringSafety(Train train, Section nextSection);
}
