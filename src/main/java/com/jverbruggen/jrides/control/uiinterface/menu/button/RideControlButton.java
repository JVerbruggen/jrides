package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.ButtonVisual;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface RideControlButton {
    void sendUpdate();

    void changeDisplayName(String displayName);

    void changeMaterial(Material material);

    void changeTitleColor(ChatColor color);

    void changeLore(List<String> lore);

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    void setVisible(boolean visible);

    void setParentMenu(RideControlMenu parentMenu);

    RideControlMenu getParentMenu();

    UUID getUuid();

    int getSlot();

    void press(Player player);

    ButtonVisual getActiveVisual();
    void updateVisual();
}
