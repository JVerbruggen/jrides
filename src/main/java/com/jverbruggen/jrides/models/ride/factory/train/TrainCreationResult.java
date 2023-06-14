package com.jverbruggen.jrides.models.ride.factory.train;

import com.jverbruggen.jrides.animator.TrainHandle;

import javax.annotation.Nonnull;
import java.util.List;

public record TrainCreationResult(@Nonnull List<TrainHandle> output, boolean success) {

}
