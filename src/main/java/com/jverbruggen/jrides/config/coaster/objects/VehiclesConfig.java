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

import org.bukkit.configuration.ConfigurationSection;

public class VehiclesConfig extends BaseConfig {
    private final int trains;
    private final int carts;
    private final int cartDistance;

    public VehiclesConfig(int trains, int carts, int cartDistance) {
        this.trains = trains;
        this.carts = carts;
        this.cartDistance = cartDistance;
    }

    public int getCarts() {
        return carts;
    }

    public int getTrains() {
        return trains;
    }

    public int getCartDistance() {
        return cartDistance;
    }

    public static VehiclesConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        int trains = getInt(configurationSection, "trains", 1);
        int carts = getInt(configurationSection, "carts", 1);
        int cartDistance = getInt(configurationSection, "cartDistance", 20);

        if(cartDistance < 20)
            throw new RuntimeException("Cart Distances for less than 20 frames are not supported");

        return new VehiclesConfig(trains, carts, cartDistance);
    }
}
