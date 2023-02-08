package com.jverbruggen.jrides.event.ride;

import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class OnrideMusicTriggerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String musicResource;
    private final List<Player> players;

    public OnrideMusicTriggerEvent(String musicResource, List<Player> players) {
        this.musicResource = musicResource;
        this.players = players;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getMusicResource() {
        return musicResource;
    }

    public List<Player> getPlayers(){
        return players;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
