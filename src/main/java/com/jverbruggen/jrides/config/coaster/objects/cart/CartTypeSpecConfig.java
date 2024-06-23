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

package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class CartTypeSpecConfig extends BaseConfig {
    private final ModelConfig model;
    private final CartSeatsConfig seats;

    public CartTypeSpecConfig(ModelConfig model, CartSeatsConfig seats) {
        this.model = model;
        this.seats = seats;
    }

    public ModelConfig getModel() {
        return model;
    }

    public CartSeatsConfig getSeats() {
        return seats;
    }

    public static CartTypeSpecConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        ModelConfig model = ModelConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "model"));
        CartSeatsConfig seats = CartSeatsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "seats"));
        return new CartTypeSpecConfig(model, seats);
    }
}
