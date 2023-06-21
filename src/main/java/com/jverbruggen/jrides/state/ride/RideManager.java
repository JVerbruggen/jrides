package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.ride.RideConfigObject;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.exception.CoasterLoadException;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.coaster.CoasterLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RideManager {
    private final JRidesLogger logger;
    private final List<CoasterHandle> coasterHandles;
    private final ConfigManager configManager;
    private final CoasterLoader coasterLoader;
    private final List<String> rideIdentifiers;

    public RideManager() {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.coasterHandles = new ArrayList<>();
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.coasterLoader = ServiceProvider.getSingleton(CoasterLoader.class);
        this.rideIdentifiers = new ArrayList<>();
    }

    public Ride GetRide(RideIdentifier identifier){
        return null;
    }

    public void addRideHandle(CoasterHandle coasterHandle){
        coasterHandles.add(coasterHandle);
    }

    public List<String> getRideIdentifiers() {
        return rideIdentifiers;
    }

    public List<CoasterHandle> getCoasterHandles() {
        return coasterHandles;
    }

    public List<RideHandle> getRideHandles(){
        return new ArrayList<>(coasterHandles);
    }

    public @Nullable CoasterHandle getRideHandle(String identifier){
        return this.coasterHandles
                .stream()
                .filter(ch -> ch.getRide().getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    public void initAllRides(World world){
        RideConfig rideConfig = configManager.getRideConfig();
        if(rideConfig == null) return;

        List<RideConfigObject> rideConfigObjects = rideConfig.getRides();

        for (RideConfigObject rideConfigObject : rideConfigObjects) {
            String rideIdentifier = rideConfigObject.getIdentifier();
            String rideType = rideConfigObject.getType();

            if(rideIdentifiers.contains(rideIdentifier)) throw new RuntimeException("Ride " + rideIdentifier + " identifier already exists!");
            rideIdentifiers.add(rideIdentifier);

            logger.info("Initialising ride " + rideIdentifier + " with type " + rideType);

            if(rideType.equals("coaster")) {
                try {
                    loadCoaster(world, rideIdentifier);
                } catch (CoasterLoadException e) {
                    JRidesPlugin.getLogger().severe("Could not load coaster " + rideIdentifier);
                }
            }else throw new RuntimeException("Ride type unknown: " + rideType);
        }

        start();
        RideInitializedEvent.send();
    }

    public void unloadAllRides(){
        getRideHandles().forEach(r -> r.unload(true));
    }

    private void loadCoaster(World world, String rideIdentifier) throws CoasterLoadException {
        RideState rideState = RideState.load(rideIdentifier);
        if(!rideState.shouldLoadRide()){
            logger.warning("Not loading ride " + rideIdentifier);
            return;
        }

        CoasterConfig coasterConfig = configManager.getCoasterConfig(rideIdentifier);
        if(coasterConfig == null) {
            logger.warning("Coaster '" + rideIdentifier + "' has no config file present, not loading");
            return;
        }

        CoasterHandle rideHandle = coasterLoader.loadCoaster(coasterConfig, rideIdentifier, rideState, world);
        if(rideHandle != null) this.addRideHandle(rideHandle);
    }

    private void start(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L);
    }

    private void tick(){
        for(CoasterHandle coasterHandle : coasterHandles){
            coasterHandle.tick();
        }
    }
}
