package com.jverbruggen.jrides.control.uiinterface.menu.button.action;

import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.models.entity.Player;

import java.util.function.Consumer;

public class RunnableButtonAction implements RideControlButtonAction {
    private Consumer<Player> consumerFunction;

    public RunnableButtonAction(Consumer<Player> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void run(Player player, RideControlButton button) {
        consumerFunction.accept(player);
    }
}
