package com.jverbruggen.jrides.animator.tool;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class ParticleTrackVisualisationTool extends ParticleVisualisationTool {
    private List<Location> locations;
    private World world;

    public ParticleTrackVisualisationTool(World world, List<Location> locations){
        super(5);
        this.world = world;
        this.locations = locations;
    }

    public static ParticleTrackVisualisationTool fromTrack(World world, Track track, int takeOneInX){
        List<NoLimitsExportPositionRecord> positions = track.getRawPositions();



        List<Location> locations = positions.stream()
                .filter(p -> p.getIndex() % takeOneInX == 0)
                .map(p -> new Location(world, p.getPosX(), p.getPosY(), p.getPosZ()))
                .collect(Collectors.toList());
        return new ParticleTrackVisualisationTool(world, locations);
    }

    @Override
    public void tick(){
        for(Player viewer : getViewers()){
            spawnVisualisationParticles(viewer);
        }
    }

    public void spawnVisualisationParticles(Player player){
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        for(Location location : locations){
            bukkitPlayer.spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 0.01, 0.01, 0.01, 0);
        }
    }
}
