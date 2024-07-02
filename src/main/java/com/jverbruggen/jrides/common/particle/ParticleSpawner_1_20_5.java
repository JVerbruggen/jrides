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

package com.jverbruggen.jrides.common.particle;

public class ParticleSpawner_1_20_5 extends ParticleSpawner_Pre_1_20_5 {

    @Override
    protected org.bukkit.Particle getBukkitParticle(Particle particle){
        String particleName = switch (particle){
            case TRACK_PARTICLE -> "HAPPY_VILLAGER";
            case SECTION_DIVIDER_PARTICLE -> "CRIT";
            case TRAIN_HEAD_PARTICLE -> "DRIPPING_WATER";
            case TRAIN_TAIL_PARTICLE -> "DRIPPING_LAVA";
            case CART_PARTICLE -> "HAPPY_VILLAGER";
            case CART_WHEEL_DISTANCE_PARTICLE -> "HEART";
        };
        return org.bukkit.Particle.valueOf(particleName);
    }
}
