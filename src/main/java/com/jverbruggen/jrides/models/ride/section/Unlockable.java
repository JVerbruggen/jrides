package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface Unlockable {
    void unlock(Train authority);
}
