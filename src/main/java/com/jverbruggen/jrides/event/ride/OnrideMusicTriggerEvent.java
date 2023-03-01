package com.jverbruggen.jrides.event.ride;

import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class OnrideMusicTriggerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final List<Player> players;
    private final String resource;
    private final String descriptor;

    public OnrideMusicTriggerEvent(List<Player> players, String resource, String descriptor) {
        this.players = players;
        this.resource = resource;
        this.descriptor = descriptor;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public String getResource() {
        return resource;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
