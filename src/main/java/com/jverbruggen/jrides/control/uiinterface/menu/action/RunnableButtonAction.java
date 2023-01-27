package com.jverbruggen.jrides.control.uiinterface.menu.action;

import com.jverbruggen.jrides.control.uiinterface.menu.RideControlButtonAction;
import com.jverbruggen.jrides.models.entity.Player;

import java.util.function.Consumer;

public class RunnableButtonAction implements RideControlButtonAction {
    private Consumer<Player> consumerFunction;

    public RunnableButtonAction(Consumer<Player> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void run(Player player) {
        consumerFunction.accept(player);
    }
}
