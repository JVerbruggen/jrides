package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.models.entity.Player;

public interface Packet {
    boolean send(Player player);
}
