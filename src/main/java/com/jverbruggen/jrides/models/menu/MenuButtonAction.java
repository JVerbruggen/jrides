package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.models.entity.Player;

public interface MenuButtonAction {
    void run(Player player, MenuButton button);
}
