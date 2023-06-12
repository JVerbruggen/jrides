package com.jverbruggen.jrides.models.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ButtonVisual {
    ChatColor getButtonDisplayNameColor();
    Material getButtonMaterial();
    String getValue();
    ItemStack toItemStack();
    boolean update();
    boolean hasUpdate();
    List<String> getLore();
    void clearUpdate();
    boolean needsFullItemStackReload();
}
