package com.jverbruggen.jrides.event.ride;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnrideMusicTriggerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String musicResource;

    public OnrideMusicTriggerEvent(String musicResource) {
        this.musicResource = musicResource;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getMusicResource() {
        return musicResource;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
