package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.models.entity.Player;

import java.util.List;

public interface Packet {
    boolean send(Player player);
    void sendAll(List<Player> players);
}
