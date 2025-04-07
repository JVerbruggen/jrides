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

import org.bukkit.Bukkit;

public class ParticleSpawnerFactory {
    public static ParticleSpawner getParticleSpawner(){
        String currentVersion = Bukkit.getVersion();
        ParticleSpawner particleSpawner = null;

        if(currentVersion.contains("1.19.2")
                || currentVersion.contains("1.20.1")
                || currentVersion.contains("1.20.2")
                || currentVersion.contains("1.20.3")
                || currentVersion.contains("1.20.4")){
            particleSpawner = new ParticleSpawner_Pre_1_20_5();
        }else if(currentVersion.contains("1.20.5")
                || currentVersion.contains("1.20.6")){
            particleSpawner = new ParticleSpawner_1_20_5();
        }

        if(particleSpawner == null)
            throw new RuntimeException("No particle spawner implemented for bukkit version '" + currentVersion + "'");

        return particleSpawner;
    }
}
