package com.jverbruggen.jrides.packets;

import org.bukkit.entity.Player;

public interface Packet {
    boolean send(Player player);
}
