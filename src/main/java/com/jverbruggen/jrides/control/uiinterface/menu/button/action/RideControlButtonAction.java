package com.jverbruggen.jrides.control.uiinterface.menu.button.action;

import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.models.entity.Player;

public interface RideControlButtonAction {
    void run(Player player, RideControlButton button);
}
