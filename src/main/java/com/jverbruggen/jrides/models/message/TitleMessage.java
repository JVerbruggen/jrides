package com.jverbruggen.jrides.models.message;

import com.jverbruggen.jrides.models.entity.Player;

public class TitleMessage implements Message {
    private String title;
    private String subtitle;
    private int fadeIn = 1;
    private int stay = 5;
    private int fadeOut = 1;

    public TitleMessage(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public TitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void send(Player player) {
        player.getBukkitPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
