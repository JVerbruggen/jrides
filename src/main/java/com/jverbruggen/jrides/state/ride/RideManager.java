package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.ride.RideConfigObject;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.SemiAutomaticMode;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.factory.ConfigCircularNoInterruptionTrackFactory;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import com.jverbruggen.jrides.models.ride.factory.TrackFactory;
import com.jverbruggen.jrides.models.ride.factory.TrainFactory;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
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

public class RideManager {
    private final JRidesLogger logger;
    private List<CoasterHandle> coasterHandles;
    private final File dataFolder;
    private final ConfigManager configManager;
    private final ViewportManager viewportManager;
    private final TrainFactory trainFactory;
    private final SeatFactory seatFactory;
    private final EffectTriggerFactory effectTriggerFactory;
    private List<String> rideIdentifiers;

    public RideManager(File dataFolder) {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.coasterHandles = new ArrayList<>();
        this.dataFolder = dataFolder;
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.trainFactory = ServiceProvider.getSingleton(TrainFactory.class);
        this.seatFactory = ServiceProvider.getSingleton(SeatFactory.class);
        this.effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);
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
        EffectTriggerCollection effectTriggerCollection = effectTriggerFactory.getEffectTriggers(rideIdentifier, startOffset);
        CoasterHandle coasterHandle = new CoasterHandle(ride, world, effectTriggerCollection);

        Track track = loadCoasterTrackFromConfig(coasterHandle, coasterConfig, offsetX, offsetY, offsetZ, startOffset);
        if(track == null) return;
        SectionProvider sectionProvider = new SectionProvider(track);
        coasterHandle.setTrack(track);

        int trainCount = coasterConfig.getVehicles().getTrains();
        List<TrainHandle> trainHandles = createTrains(track, coasterConfig, sectionProvider, rideIdentifier, trainCount);
        coasterHandle.setTrains(trainHandles);

        StationHandle stationHandle = coasterHandle.getStationHandle(null);

//        ControlMode controlMode = new AutomaticMode(
//                stationHandle,
//                coasterHandle.getDispatchTrigger().getDispatchLockCollection());
        ControlMode controlMode = new SemiAutomaticMode(
                stationHandle,
                coasterHandle.getDispatchTrigger().getDispatchLockCollection());

        RideController rideController = new RideController(controlMode);
        rideController.setRideHandle(coasterHandle);
        coasterHandle.setRideController(rideController);

        coasterHandle.start();
        this.addRideHandle(coasterHandle);
    }

    private TrainHandle createTrain(Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String trainIdentifier){
        Train train = trainFactory.createEquallyDistributedTrain(track, coasterConfig, trainIdentifier);
        if(train == null) return null;

        return new TrainHandle(sectionProvider, train, track);
    }

    private List<TrainHandle> createTrains(Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String rideIdentifier, int count){
        List<TrainHandle> trains = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String trainName = rideIdentifier + ":train_" + (i+1);
            TrainHandle trainHandle = createTrain(track, coasterConfig, sectionProvider, trainName);
            if(trainHandle != null)
                trains.add(trainHandle);
        }
        return trains;
    }

    private Track loadCoasterTrackFromConfig(CoasterHandle coasterHandle, CoasterConfig coasterConfig, float offsetX, float offsetY, float offsetZ, int startOffset){
        Ride ride = coasterHandle.getRide();
        String rideIdentifier = ride.getIdentifier();
        String configFileName = configManager.getFolder(rideIdentifier) + "/" + rideIdentifier + ".csv";
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

        Frame startFrame = new SimpleFrame(startOffset);
        TrackFactory trackFactory = new ConfigCircularNoInterruptionTrackFactory(coasterHandle, coasterConfig, new TrackDescription(positions, TrackType.TRACK, startFrame, startFrame), startOffset);
        return trackFactory.createTrack();
    }
}
