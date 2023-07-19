package com.jverbruggen.jrides.models.map;

import java.util.Collection;

import com.jverbruggen.jrides.models.entity.Player;

public interface VirtualMap {
    void updateVisuals();
    void sendUpdate(Collection<Player> players);
    void give(Player player);
}
