package com.jverbruggen.jrides.control.uiinterface.menu.button.action;

import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.models.entity.Player;

import java.util.function.BiConsumer;

public class RunnableButtonWithContextAction implements RideControlButtonAction {
    private BiConsumer<Player, RideControlButton> consumerFunction;

    public RunnableButtonWithContextAction(BiConsumer<Player, RideControlButton> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void run(Player player, RideControlButton button) {
        consumerFunction.accept(player, button);
    }
}
