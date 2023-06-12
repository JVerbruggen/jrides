package com.jverbruggen.jrides.control.uiinterface.menu.button.action;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.MenuButtonAction;

import java.util.function.BiConsumer;

public class RunnableButtonWithContextAction implements MenuButtonAction {
    private BiConsumer<Player, MenuButton> consumerFunction;

    public RunnableButtonWithContextAction(BiConsumer<Player, MenuButton> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void run(Player player, MenuButton button) {
        consumerFunction.accept(player, button);
    }
}
