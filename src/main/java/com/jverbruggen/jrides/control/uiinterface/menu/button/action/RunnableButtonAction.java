package com.jverbruggen.jrides.control.uiinterface.menu.button.action;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.MenuButtonAction;

import java.util.function.Consumer;

public class RunnableButtonAction implements MenuButtonAction {
    private Consumer<Player> consumerFunction;

    public RunnableButtonAction(Consumer<Player> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void run(Player player, MenuButton button) {
        consumerFunction.accept(player);
    }
}
