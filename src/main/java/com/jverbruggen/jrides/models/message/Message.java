package com.jverbruggen.jrides.models.message;

import com.jverbruggen.jrides.models.entity.Player;

public interface Message {
    void send(Player player);
}
