package com.jverbruggen.jrides.nms;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

public interface NMSHandler {
    void setPlayerLocationNoTeleport(Player player, Vector3 position);
}
