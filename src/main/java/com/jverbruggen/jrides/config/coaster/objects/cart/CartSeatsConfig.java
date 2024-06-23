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
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class CartSeatsConfig extends BaseConfig {
    private List<Vector3PlusYaw> positions;

    public CartSeatsConfig(List<Vector3PlusYaw> positions) {
        this.positions = positions;
    }

    public List<Vector3PlusYaw> getPositions() {
        return positions;
    }

    public static CartSeatsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<List<Double>> positions = getDoubleListList(configurationSection, "positions", List.of());
        List<Vector3PlusYaw> vectors = positions.stream()
                .map(Vector3PlusYaw::fromDoubleList)
                .collect(Collectors.toList());
        return new CartSeatsConfig(vectors);
    }
}
