/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.control.uiinterface.menu.button.common;

import com.jverbruggen.jrides.models.menu.ButtonVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StatefulButtonVisual<T> implements ButtonVisual {

    private final Supplier<T> state;
    private final Map<T, ButtonVisual> options;
    private final T _default;
    private @Nonnull ButtonVisual current;

    private boolean hasUpdate;

    public StatefulButtonVisual(Supplier<T> state, Map<T, ButtonVisual> options, T _default) {
        this.state = state;
        this.options = options;
        this._default = _default;
        this.current = options.get(_default);
        this.hasUpdate = false;
    }

    public void updateCurrent(){
        ButtonVisual newCurrent = options.get(state.get());
        if(newCurrent == null)
            throw new RuntimeException("StatefulButtonVisual wanted to update to a visual option that did not exist");

        if(current != newCurrent)
            hasUpdate = true;

        current = newCurrent;
    }


    @Override
    public ChatColor getButtonDisplayNameColor() {
        return current.getButtonDisplayNameColor();
    }

    @Override
    public Material getButtonMaterial() {
        return current.getButtonMaterial();
    }

    @Override
    public String getValue() {
        return current.getValue();
    }

    @Override
    public ItemStack toItemStack() {
        return current.toItemStack();
    }

    @Override
    public boolean update() {
        updateCurrent();
        current.update();
        return true;
    }

    @Override
    public boolean hasUpdate() {
        return current.hasUpdate() || hasUpdate;
    }

    @Override
    public List<String> getLore() {
        return current.getLore();
    }

    @Override
    public void clearUpdate() {
        current.clearUpdate();
        hasUpdate = false;
    }

    @Override
    public boolean needsFullItemStackReload() {
        return current.needsFullItemStackReload();
    }
}
