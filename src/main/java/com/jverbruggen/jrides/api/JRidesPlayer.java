package com.jverbruggen.jrides.api;

import java.util.UUID;

public interface JRidesPlayer {
    org.bukkit.entity.Player getBukkitPlayer();
    String getName();
    String getIdentifier();
    UUID getUniqueId();
    boolean isSeated();

    void sendTitle(String title, String subtitle, int stay);
}
