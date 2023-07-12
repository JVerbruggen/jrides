package com.jverbruggen.jrides.api;

import org.bukkit.entity.Player;

public class JRidesPlayerMock implements JRidesPlayer {
    private boolean seated = false;

    @Override
    public Player getBukkitPlayer() {
        throw new UnsupportedOperationException("Unimplemented method 'getBukkitPlayer'");
    }

    @Override
    public String getName() {
        return "MockPlayer";
    }

    @Override
    public String getIdentifier() {
        return "mockplayer";
    }

    @Override
    public boolean isSeated() {
        return this.seated;
    }

    @Override
    public void sendTitle(String title, String subtitle, int stay) {
        return;
    }
    
}
