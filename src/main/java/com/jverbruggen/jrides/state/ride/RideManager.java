package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.animator.GCRideHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.ride.RideConfigObject;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.factory.TrackFactory;
import com.jverbruggen.jrides.models.ride.factory.TrainFactory;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RideManager {
    private final Logger logger;
    private List<GCRideHandle> rideHandles;
    private final File dataFolder;
    private final ConfigManager configManager;
    private final ViewportManager viewportManager;
    private final TrainFactory trainFactory;
    private final TrackFactory trackFactory;

    public RideManager(Logger logger, File dataFolder, ViewportManager viewportManager, ConfigManager configManager,
                       TrainFactory trainFactory, TrackFactory trackFactory) {
        this.logger = logger;
        this.rideHandles = new ArrayList<>();
        this.dataFolder = dataFolder;
        this.viewportManager = viewportManager;
        this.configManager = configManager;
        this.trainFactory = trainFactory;
        this.trackFactory = trackFactory;
    }

    public Ride GetRide(RideIdentifier identifier){
        return null;
    }

    public void addRideHandle(GCRideHandle rideHandle){
        rideHandles.add(rideHandle);
    }

    public GCRideHandle getRideHandle(String identifier){
        return this.rideHandles.get(0);
    }

    public void initAllRides(World world){
        RideConfig rideConfig = configManager.getRideConfig();
        List<RideConfigObject> rideConfigObjects = rideConfig.getRides();

        for (RideConfigObject rideConfigObject : rideConfigObjects) {
            String rideIdentifier = rideConfigObject.getIdentifier();
            String rideType = rideConfigObject.getType();

            logger.info("Initialising ride " + rideIdentifier + " with type " + rideType);

            if(rideType.equals("coaster")) {
                loadCoaster(world, rideIdentifier);
            }else throw new RuntimeException("Ride type unknown: " + rideType);
        }
    }

    private void loadCoaster(World world, String rideIdentifier){
        CoasterConfig coasterConfig = configManager.getCoasterConfig(rideIdentifier);

        Ride ride = null;
        List<Float> offset = coasterConfig.getTrack().getPosition();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);

        int startOffset = 4000;

        Track track = loadCoasterTrackFromConfig(world, rideIdentifier, offsetX, offsetY, offsetZ, startOffset);

        Train train = trainFactory.createEquallyDistributedTrain(track);
        TrainHandle trainHandle = new TrainHandle(train, track, startOffset);
        GCRideHandle rideHandle = new GCRideHandle(ride, List.of(trainHandle), track, world);
        this.addRideHandle(rideHandle);
    }

    private Track loadCoasterTrackFromConfig(World world, String rideIdentifier, float offsetX, float offsetY, float offsetZ, int startOffset){
        String configFileName = "coasters/" + rideIdentifier + ".csv";
        File configFile = new File(dataFolder, configFileName);
        Path pathToConfigFile = configFile.toPath();
        List<NoLimitsExportPositionRecord> positions = new ArrayList<>();

        try(BufferedReader br = Files.newBufferedReader(pathToConfigFile, StandardCharsets.UTF_8)){
            String line = br.readLine(); // Skip header line
            line = br.readLine();
            while(line != null){
                String[] attributes = line.split("\t");
                NoLimitsExportPositionRecord record = NoLimitsExportPositionRecord.createFromCSVAttributes(attributes, offsetX, offsetY, offsetZ);
                positions.add(record);

                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return trackFactory.createSimpleTrack(positions, startOffset);
    }
}
