package com.jverbruggen.jrides.event.ride;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class ExternalTriggerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String trainName;
    private final Map<String, String> data;

    public ExternalTriggerEvent(String trainName, Map<String, String> data) {
        this.trainName = trainName;
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getTrainName() {
        return trainName;
    }

    public Map<String, String> getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
