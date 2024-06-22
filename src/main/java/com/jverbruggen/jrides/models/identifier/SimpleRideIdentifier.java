package com.jverbruggen.jrides.models.identifier;

public class SimpleRideIdentifier implements RideIdentifier {
    private String name;

    public SimpleRideIdentifier(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifierString() {
        return name;
    }
}
