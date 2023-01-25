package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;

import java.util.Map;
import java.util.Set;

public class TrainHandle {
    private Train train;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track) {
        this.sectionProvider = sectionProvider;
        this.train = train;
        this.track = track;
        this.trackBehaviour = train.getHeadSection().getTrackBehaviour();
        this.speedBPS = new Speed(0);
    }

    public void tick(){
        if(train.isCrashed()) return;

        Section newSection = sectionProvider.getSectionFor(train, train.getHeadOfTrainFrame());
        if(newSection != train.getHeadSection()){
            if(newSection.isOccupied()){
                train.setCrashed(true);
                JRidesPlugin.getLogger().warning(LogType.CRASH, "Train " + train + " has crashed!");
                JRidesPlugin.getLogger().warning(LogType.CRASH, train.getHeadSection().toString());
                JRidesPlugin.getLogger().warning(LogType.CRASH, newSection.toString());
                return;
            }

            // TODO: If moving forward this is only true
            newSection.addOccupation(train);
            train.addCurrentSection(newSection);
            trackBehaviour = newSection.getTrackBehaviour();
        }

        Section tailSection = sectionProvider.getSectionFor(train, train.getTailOfTrainFrame());
        if(tailSection != train.getTailSection()){
            Section oldTailSection = train.getTailSection();
            // TODO: If moving forward this is only true
            oldTailSection.removeOccupation(train);
            train.removeCurrentSection(oldTailSection);
            oldTailSection.getTrackBehaviour().trainExitedAtEnd();
        }

        TrainMovement result = trackBehaviour.move(speedBPS, train.getCurrentLocation(), train, track);
        speedBPS = result.getNewSpeed();
        train.getHeadOfTrainFrame().updateTo(result.getNewHeadOfTrainFrame());

        Set<Map.Entry<Cart, CartMovement>> cartMovements = result.getCartMovements();
        if(cartMovements != null){
            for(Map.Entry<Cart, CartMovement> cartMovement : cartMovements){
                Cart cart = cartMovement.getKey();
                CartMovement movement = cartMovement.getValue();
                cart.setPosition(movement);
            }
        }

        Vector3 newLocation = result.getNewTrainLocation();
        train.setCurrentLocation(newLocation);

//        World world = Bukkit.getWorld("world");
//        world.spawnParticle(Particle.DRIP_LAVA, newLocation.toBukkitLocation(world), 5, 0.01, 1, 0.01, 0);
//        world.spawnParticle(Particle.DRIP_WATER, track.getRawPositions().get(train.getHeadOfTrainFrame().getValue()).toVector3().toBukkitLocation(world), 5, 0.01, 1, 0.01, 0);
    }
}
