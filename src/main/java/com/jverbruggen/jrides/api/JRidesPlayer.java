package com.jverbruggen.jrides.api;

public interface JRidesPlayer {
    org.bukkit.entity.Player getBukkitPlayer();
    String getName();
    String getIdentifier();
    boolean isSeated();
}