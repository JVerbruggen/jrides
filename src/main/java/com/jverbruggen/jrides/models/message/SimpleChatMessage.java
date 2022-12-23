package com.jverbruggen.jrides.models.message;

import com.jverbruggen.jrides.models.entity.Player;

public class SimpleChatMessage implements Message {
    private String contents;

    public SimpleChatMessage(String contents) {
        this.contents = contents;
    }

    @Override
    public void send(Player player) {
        player.getBukkitPlayer().sendMessage(contents);
    }
}
