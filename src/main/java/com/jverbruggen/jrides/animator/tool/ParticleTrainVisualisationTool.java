package com.jverbruggen.jrides.animator.tool;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.Train;
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
//            spawnVisualisationParticles(viewer);
        }
    }

//    public void spawnVisualisationParticles(Player player){
//        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
//        bukkitPlayer.spawnParticle(Particle.VILLAGER_HAPPY, headOfTrainLocation, 1, 0.01, 0.01, 0.01, 0);
//    }
}
