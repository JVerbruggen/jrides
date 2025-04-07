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

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.common.particle.Particle;
import com.jverbruggen.jrides.common.particle.ParticleSpawner;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParticleTrackVisualisationTool extends ParticleVisualisationTool {
    private final List<Vector3> locations;
    private final List<Vector3> sectionSplitLocations;
    private final List<TrainHandle> trains;
    private final ParticleSpawner particleSpawner;

    public ParticleTrackVisualisationTool(List<Vector3> locations, List<Vector3> sectionSplitLocations, List<TrainHandle> trains){
        super(5);
        this.locations = locations;
        this.sectionSplitLocations = sectionSplitLocations;
        this.trains = trains;
        this.particleSpawner = ServiceProvider.getSingleton(ParticleSpawner.class);
    }

    public static ParticleTrackVisualisationTool fromTrack(Track track, int takeOneInX, List<TrainHandle> trains){
        List<Vector3> positions = track.getAllPositions();

        List<Vector3> sectionSplitLocations = track.getSections().stream()
                .map(s -> s.getParentTrack().getLocationFor(s.getEndFrame()))
                .collect(Collectors.toList());

        List<Vector3> locations = IntStream
                .range(0, positions.size())
                .filter(i -> i % takeOneInX == 0)
                .mapToObj(positions::get)
                .collect(Collectors.toList());

        return new ParticleTrackVisualisationTool(locations, sectionSplitLocations, trains);
    }

    @Override
    public void tick(){
        for(Player viewer : getViewers()){
            spawnVisualisationParticles(viewer);
        }
    }

    public void spawnVisualisationParticles(Player player){
        for(Vector3 location : locations){
            particleSpawner.spawnParticle(player, Particle.TRACK_PARTICLE, location, 1, 0.01, 0.01, 0.01);
        }
        for(Vector3 splitSectionLocation : sectionSplitLocations){
            particleSpawner.spawnParticle(player, Particle.SECTION_DIVIDER_PARTICLE, splitSectionLocation, 5, 0.01, 1, 0.01);
        }
        for(TrainHandle train : trains){
            particleSpawner.spawnParticle(player, Particle.TRAIN_HEAD_PARTICLE, train.getTrain().getCurrentHeadLocation(), 5, 0.01, 1, 0.01);
            for(CoasterCart cart : train.getTrain().getCarts()){
                particleSpawner.spawnParticle(player, Particle.CART_PARTICLE, cart.getPosition(), 5, 0.01, 1, 0.01);

                Section cartSection = cart.getFrame().getSection();
                if(cart.getWheelDistance() != 0) {
                    particleSpawner.spawnParticle(player, Particle.CART_WHEEL_DISTANCE_PARTICLE, cartSection.getLocationFor(cart.getFrame().clone().add(cart.getWheelDistance())), 1, 0.01, 1, 0.01);
                    particleSpawner.spawnParticle(player, Particle.CART_WHEEL_DISTANCE_PARTICLE, cartSection.getLocationFor(cart.getFrame().clone().add(-cart.getWheelDistance())), 1, 0.01, 1, 0.01);
                }
            }
            particleSpawner.spawnParticle(player, Particle.TRAIN_TAIL_PARTICLE, train.getTrain().getCurrentTailLocation(), 5, 0.01, 1, 0.01);
        }
    }
}
