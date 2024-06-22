package com.jverbruggen.jrides.control.uiinterface.menu.button.common;

import com.jverbruggen.jrides.models.menu.ButtonVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlinkingButtonVisual implements ButtonVisual {
    private final StaticButtonVisual blinkLowVisual;
    private final StaticButtonVisual blinkHighVisual;
    private boolean blinkHigh;
    private StaticButtonVisual activeVisual;
    private boolean hasUpdate;

    public BlinkingButtonVisual(StaticButtonVisual blinkLowVisual, StaticButtonVisual blinkHighVisual) {
        this.blinkLowVisual = blinkLowVisual;
        this.blinkHighVisual = blinkHighVisual;
        this.activeVisual = blinkLowVisual;
        this.blinkHigh = false;
        this.hasUpdate = false;
    }

    private ButtonVisual updateButtonVisual(){
        this.blinkHigh = !this.blinkHigh;
        this.hasUpdate = true;

        this.activeVisual = blinkHigh ? blinkHighVisual : blinkLowVisual;
        return activeVisual;
    }

    public ChatColor getButtonDisplayNameColor() {
        return activeVisual.getButtonDisplayNameColor();
    }

    public Material getButtonMaterial() {
        return activeVisual.getButtonMaterial();
    }

    public ItemStack toItemStack(){
        return activeVisual.toItemStack();
    }

    @Override
    public boolean update() {
        updateButtonVisual();
        return true;
    }

    @Override
    public boolean hasUpdate() {
        return hasUpdate;
    }

    @Override
    public List<String> getLore() {
        return activeVisual.getLore();
    }

    @Override
    public void clearUpdate() {
        hasUpdate = false;
    }

    @Override
    public boolean needsFullItemStackReload() {
        return false;
    }

    @Override
    public String getValue() {
        return activeVisual.getValue();
    }
}
