package com.jverbruggen.jrides.models.entity.agent;

import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.state.player.PlayerManager;

public interface MessageAgent extends MessageReceiver {
    boolean hasPermission(String permission);
    boolean isPlayer();
    Player getPlayer(PlayerManager playerManager);
}
