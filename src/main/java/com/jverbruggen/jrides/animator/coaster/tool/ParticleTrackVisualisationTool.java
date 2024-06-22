package com.jverbruggen.jrides.animator.coaster.tool;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParticleTrackVisualisationTool extends ParticleVisualisationTool {
    private List<Location> locations;
    private World world;
    private List<Location> sectionSplitLocations;
    private List<TrainHandle> trains;

    public ParticleTrackVisualisationTool(World world, List<Location> locations, List<Location> sectionSplitLocations, List<TrainHandle> trains){
        super(5);
        this.world = world;
        this.locations = locations;
        this.sectionSplitLocations = sectionSplitLocations;
        this.trains = trains;
    }

    public static ParticleTrackVisualisationTool fromTrack(World world, Track track, int takeOneInX, List<TrainHandle> trains){
        List<Vector3> positions = track.getAllPositions();

        List<Location> sectionSplitLocations = track.getSections().stream()
                .map(s -> s.getParentTrack().getLocationFor(s.getEndFrame()).toBukkitLocation(world))
                .collect(Collectors.toList());

        List<Location> locations = IntStream
                .range(0, positions.size())
                .filter(i -> i % takeOneInX == 0)
                .mapToObj(i -> positions.get(i).toBukkitLocation(world))
                .collect(Collectors.toList());

        return new ParticleTrackVisualisationTool(world, locations, sectionSplitLocations, trains);
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
        for(Location splitSectionLocation : sectionSplitLocations){
            bukkitPlayer.spawnParticle(Particle.CRIT_MAGIC, splitSectionLocation, 5, 0.01, 1, 0.01, 0);
        }
        for(TrainHandle train : trains){
            bukkitPlayer.spawnParticle(Particle.DRIP_WATER, train.getTrain().getCurrentHeadLocation().toBukkitLocation(world), 5, 0.01, 1, 0.01, 0);
            for(CoasterCart cart : train.getTrain().getCarts()){
                bukkitPlayer.spawnParticle(Particle.VILLAGER_HAPPY, cart.getPosition().toBukkitLocation(world), 5, 0.01, 1, 0.01, 0);
            }
            bukkitPlayer.spawnParticle(Particle.DRIP_LAVA, train.getTrain().getCurrentTailLocation().toBukkitLocation(world), 5, 0.01, 1, 0.01, 0);
        }
    }
}
