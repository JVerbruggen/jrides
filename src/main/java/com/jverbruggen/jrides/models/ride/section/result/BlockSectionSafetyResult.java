package com.jverbruggen.jrides.models.ride.section.result;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public record BlockSectionSafetyResult(boolean safe, @Nullable Train forTrain, @Nonnull String reason) {
    public static BlockSectionSafetyResult emptyFalse(){
        return new BlockSectionSafetyResult(false, null, "");
    }

    @Override
    public String reason() {
        String trainName = forTrain != null ? forTrain.getName() : "null";
        String sections = forTrain != null ? String.join(",", forTrain.getCurrentSections().stream().map(Object::toString).toList()) : "null";
        return "(" + trainName + ") [" + sections + "] " + reason;
    }

    @Override
    public String toString() {
        return reason();
    }
}
