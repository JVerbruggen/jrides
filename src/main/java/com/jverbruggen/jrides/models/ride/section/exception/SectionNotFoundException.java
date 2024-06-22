package com.jverbruggen.jrides.models.ride.section.exception;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class SectionNotFoundException extends RuntimeException {
    private final Train train;

    public SectionNotFoundException(Train train) {
        this.train = train;
    }

    @Override
    public String getMessage() {
        return "Section not found for train " + this.train.toString();
    }
}
