package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.ride.RideConfigObject;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.controlmode.AutomaticMode;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.SemiAutomaticMode;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import com.jverbruggen.jrides.models.ride.factory.TrackFactory;
import com.jverbruggen.jrides.models.ride.factory.TrainFactory;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;
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
    private List<CoasterHandle> coasterHandles;
    private final File dataFolder;
    private final ConfigManager configManager;
    private final ViewportManager viewportManager;
    private final TrainFactory trainFactory;
    private final TrackFactory trackFactory;
    private final SeatFactory seatFactory;
    private List<String> rideIdentifiers;

    public RideManager(Logger logger, File dataFolder, ViewportManager viewportManager, ConfigManager configManager,
                       TrainFactory trainFactory, TrackFactory trackFactory, SeatFactory seatFactory) {
        this.logger = logger;
        this.coasterHandles = new ArrayList<>();
        this.dataFolder = dataFolder;
        this.viewportManager = viewportManager;
        this.configManager = configManager;
        this.trainFactory = trainFactory;
        this.trackFactory = trackFactory;
        this.seatFactory = seatFactory;
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

    public CoasterHandle getRideHandle(String identifier){
        return this.coasterHandles
                .stream()
                .filter(ch -> ch.getRide().getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElseThrow();
    }

    public void initAllRides(World world){
        RideConfig rideConfig = configManager.getRideConfig();
        List<RideConfigObject> rideConfigObjects = rideConfig.getRides();

        for (RideConfigObject rideConfigObject : rideConfigObjects) {
            String rideIdentifier = rideConfigObject.getIdentifier();
            String rideType = rideConfigObject.getType();

            if(rideIdentifiers.contains(rideIdentifier)) throw new RuntimeException("Ride " + rideIdentifier + " identifier already exists!");
            rideIdentifiers.add(rideIdentifier);

            logger.info("Initialising ride " + rideIdentifier + " with type " + rideType);

            if(rideType.equals("coaster")) {
                loadCoaster(world, rideIdentifier);
            }else throw new RuntimeException("Ride type unknown: " + rideType);
        }
    }

    private void loadCoaster(World world, String rideIdentifier){
        CoasterConfig coasterConfig = configManager.getCoasterConfig(rideIdentifier);

        String displayName = coasterConfig.getDisplayName();
        Vector3 warpLocation = coasterConfig.getWarpLocation();
        Ride ride = new SimpleCoaster(rideIdentifier, displayName, warpLocation);
        List<Float> offset = coasterConfig.getTrack().getPosition();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);

        int startOffset = coasterConfig.getTrack().getOffset();
        CoasterHandle coasterHandle = new CoasterHandle(ride, world);

        Track track = loadCoasterTrackFromConfig(coasterHandle, coasterConfig, offsetX, offsetY, offsetZ, startOffset);
        SectionProvider sectionProvider = new SectionProvider(track);
        coasterHandle.setTrack(track);

        List<TrainHandle> trainHandles = createTrains(track, coasterConfig, sectionProvider, rideIdentifier, 2);
        coasterHandle.setTrains(trainHandles);

        StationHandle stationHandle = coasterHandle.getStationHandle(null);

//        ControlMode controlMode = new AutomaticMode(
//                stationHandle,
//                coasterHandle.getDispatchTrigger().getDispatchLockCollection());
        ControlMode controlMode = new SemiAutomaticMode(
                stationHandle,
                coasterHandle.getDispatchTrigger().getDispatchLockCollection()
        );

        RideController rideController = new RideController(controlMode);
        rideController.setRideHandle(coasterHandle);
        coasterHandle.setRideController(rideController);

        coasterHandle.start();
        this.addRideHandle(coasterHandle);
    }

    private TrainHandle createTrain(Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String trainIdentifier){
        Train train = trainFactory.createEquallyDistributedTrain(track, coasterConfig, trainIdentifier);
        return new TrainHandle(sectionProvider, train, track);
    }

    private List<TrainHandle> createTrains(Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String rideIdentifier, int count){
        List<TrainHandle> trains = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String trainName = rideIdentifier + ":train_" + (i+1);
            trains.add(createTrain(track, coasterConfig, sectionProvider, trainName));
        }
        return trains;
    }

    private Track loadCoasterTrackFromConfig(CoasterHandle coasterHandle, CoasterConfig coasterConfig, float offsetX, float offsetY, float offsetZ, int startOffset){
        Ride ride = coasterHandle.getRide();
        String configFileName = "coasters/" + ride.getIdentifier() + ".csv";
        File configFile = new File(dataFolder, configFileName);
        Path pathToConfigFile = configFile.toPath();
        List<NoLimitsExportPositionRecord> positions = new ArrayList<>();

        try(BufferedReader br = Files.newBufferedReader(pathToConfigFile, StandardCharsets.UTF_8)){
            String line = br.readLine(); // Skip header line
            line = br.readLine();
            int index = 0;
            while(line != null){
                String[] attributes = line.split("\t");
                NoLimitsExportPositionRecord record = NoLimitsExportPositionRecord.createFromCSVAttributes(attributes, index, offsetX, offsetY, offsetZ);
                positions.add(record);

                line = br.readLine();
                index++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return trackFactory.createSimpleTrack(coasterHandle, coasterConfig, positions, startOffset);
    }
}
