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

package com.jverbruggen.jrides.animator.coaster.tool;

import com.jverbruggen.jrides.common.particle.Particle;
import com.jverbruggen.jrides.common.particle.ParticleSpawner;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class ParticleTrainVisualisationTool extends ParticleVisualisationTool {
    private final ParticleSpawner particleSpawner;
    private final Train train;

    public ParticleTrainVisualisationTool(Train train){
        super(5);
        this.particleSpawner = ServiceProvider.getSingleton(ParticleSpawner.class);
        this.train = train;
    }

    @Override
    public void tick(){
        for(Player viewer : getViewers()){
            spawnVisualisationParticles(viewer);
        }
    }

    public void spawnVisualisationParticles(Player player){
        particleSpawner.spawnParticle(player, Particle.TRAIN_HEAD_PARTICLE, train.getCurrentHeadLocation(), 1, 0.01, 0.01, 0.01);
    }
}
