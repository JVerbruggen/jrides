package com.jverbruggen.jrides.control.uiinterface.menu.button.common;

import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StaticButtonVisual implements ButtonVisual {
    private final Material buttonMaterial;
    private final ChatColor buttonDisplayNameColor;
    private final String value;
    private final List<String> lore;

    public StaticButtonVisual(Material buttonMaterial, ChatColor buttonDisplayNameColor, String value) {
        this.buttonMaterial = buttonMaterial;
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = List.of();
    }

    public StaticButtonVisual(Material buttonMaterial, ChatColor buttonDisplayNameColor, String value, List<String> lore) {
        this.buttonMaterial = buttonMaterial;
        this.buttonDisplayNameColor = buttonDisplayNameColor;
        this.value = value;
        this.lore = lore;
    }

    public ChatColor getButtonDisplayNameColor() {
        return buttonDisplayNameColor;
    }

    public Material getButtonMaterial() {
        return buttonMaterial;
    }

    @Override
    public String getValue() {
        return value;
    }

    public ItemStack toItemStack(){
        return ItemStackFactory.getRideControlButtonStack(getButtonMaterial(), buttonDisplayNameColor + value, lore);
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean hasUpdate() {
        return false;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void clearUpdate() {

    }

    @Override
    public boolean needsFullItemStackReload() {
        return false;
    }
}
