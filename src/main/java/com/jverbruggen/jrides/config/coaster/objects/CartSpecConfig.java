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

package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.cart.CartTypeSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class CartSpecConfig {
    private final CartTypeSpecConfig _default;
    private final CartTypeSpecConfig head;
    private final CartTypeSpecConfig tail;

    public CartSpecConfig(CartTypeSpecConfig _default, CartTypeSpecConfig head, CartTypeSpecConfig tail) {
        this._default = _default;
        this.head = head;
        this.tail = tail;
    }

    public CartTypeSpecConfig getDefault() {
        return _default;
    }

    public CartTypeSpecConfig getHead() {
        return head;
    }

    public CartTypeSpecConfig getTail() {
        return tail;
    }

    public boolean hasHead(){
        return head != null;
    }

    public boolean hasTail(){
        return tail != null;
    }

    public static CartSpecConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null)
            return new CartSpecConfig(CartTypeSpecConfig.fromConfigurationSection(null), null, null);

        CartTypeSpecConfig _default = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("default"));
        CartTypeSpecConfig head = null;
        CartTypeSpecConfig tail = null;

        if(configurationSection.contains("head")){
            head = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("head"));
        }
        if(configurationSection.contains("tail")){
            tail = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("tail"));
        }

        return new CartSpecConfig(_default, head, tail);
    }
}

