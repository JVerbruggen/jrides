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

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleTrainVisualisationTool extends ParticleVisualisationTool {
    private World world;
    private Train train;

    public ParticleTrainVisualisationTool(World world, Train train){
        super(5);
        this.world = world;
        this.train = train;
    }

    @Override
    public void tick(){
        for(Player viewer : getViewers()){
            spawnVisualisationParticles(viewer);
        }
    }

    public void spawnVisualisationParticles(Player player){
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        bukkitPlayer.spawnParticle(Particle.VILLAGER_HAPPY, train.getCurrentHeadLocation().toBukkitLocation(world), 1, 0.01, 0.01, 0.01, 0);
    }
}
