package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.ride.RideConfigObject;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.controller.RideControllerFactory;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.factory.*;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RideManager {
    private final JRidesLogger logger;
    private List<CoasterHandle> coasterHandles;
    private final File dataFolder;
    private final ConfigManager configManager;
    private final ViewportManager viewportManager;
    private final TrainFactory trainFactory;
    private final SeatFactory seatFactory;
    private final EffectTriggerFactory effectTriggerFactory;
    private final RideControllerFactory rideControllerFactory;
    private final RideControlMenuFactory rideControlMenuFactory;
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
        this.rideControllerFactory = ServiceProvider.getSingleton(RideControllerFactory.class);
        this.rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
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
                loadCoaster(world, rideIdentifier);
            }else throw new RuntimeException("Ride type unknown: " + rideType);
        }

        start();
        RideInitializedEvent.send();
    }

    private void loadCoaster(World world, String rideIdentifier){
        RideState rideState = RideState.load(rideIdentifier);
        if(!rideState.shouldLoadRide()){
            logger.warning("Not loading ride " + rideIdentifier);
            return;
        }

        CoasterConfig coasterConfig = configManager.getCoasterConfig(rideIdentifier);
        if(coasterConfig == null) return;

        String displayName = coasterConfig.getDisplayName();
        PlayerLocation warpLocation = coasterConfig.getWarpLocation();
        Ride ride = new SimpleCoaster(rideIdentifier, displayName, warpLocation);
        int rideOverviewMapId = coasterConfig.getRideOverviewMapId();
        List<Float> offset = coasterConfig.getTrack().getPosition();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);

        SoundsConfig sounds = coasterConfig.getSoundsConfig();
        String dispatchSound = sounds.getDispatch();
        String restraintOpenSound = sounds.getRestraintOpen();
        String restraintCloseSound = sounds.getRestraintClose();
        String windSound = sounds.getOnrideWind();

        CoasterHandle coasterHandle = new CoasterHandle(ride, world, dispatchSound, restraintOpenSound,
                restraintCloseSound, windSound, rideOverviewMapId);

        Track track = loadCoasterTrackFromConfig(coasterHandle, coasterConfig, offsetX, offsetY, offsetZ);
        if(track == null){
            logger.severe("Track was null, so coaster " + rideIdentifier + " could not be loaded");
            return;
        }
        coasterHandle.setTrack(track);

        EffectTriggerCollection effectTriggerCollection = effectTriggerFactory.getTrainEffectTriggers(rideIdentifier, track);
        coasterHandle.setEffectTriggerCollection(effectTriggerCollection);

        SectionProvider sectionProvider = new SectionProvider();
        int trainCount = coasterConfig.getVehicles().getTrains();
        List<TrainHandle> trainHandles = createTrains(coasterHandle, track, coasterConfig, sectionProvider, rideIdentifier, trainCount);
        coasterHandle.setTrains(trainHandles);

        RideController rideController = rideControllerFactory.createRideController(coasterHandle, coasterConfig.getControllerConfig());
        Menu rideControlMenu = rideControlMenuFactory.getRideControlMenu(rideController, coasterConfig.getControllerConfig());
        coasterHandle.setRideController(rideController, rideControlMenu);

        coasterHandle.setState(rideState);

        this.addRideHandle(coasterHandle);
    }

    private TrainHandle createTrain(CoasterHandle coasterHandle, Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String trainIdentifier){
        Train train = trainFactory.createEquallyDistributedTrain(coasterHandle, track, coasterConfig, trainIdentifier);
        if(train == null) return null;

        return new TrainHandle(sectionProvider, train, track);
    }

    private List<TrainHandle> createTrains(CoasterHandle coasterHandle, Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String rideIdentifier, int count){
        List<TrainHandle> trains = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String trainName = rideIdentifier + ":train_" + (i+1);
            TrainHandle trainHandle = createTrain(coasterHandle, track, coasterConfig, sectionProvider, trainName);
            if(trainHandle != null)
                trains.add(trainHandle);
            else throw new RuntimeException("TrainHandle was null!");
        }
        return trains;
    }

    private Track loadCoasterTrackFromConfig(CoasterHandle coasterHandle, CoasterConfig coasterConfig, float offsetX, float offsetY, float offsetZ){
        List<String> trackIdentifiers = coasterConfig.getTrack().getParts();
        List<TrackDescription> trackDescriptions = trackIdentifiers.stream()
                .map(identifier -> loadTrackSegmentFromConfig(identifier, coasterHandle, coasterConfig, offsetX, offsetY, offsetZ))
                .collect(Collectors.toList());

        TrackFactory trackFactory = new ConfigAdvancedSplineTrackFactory(coasterHandle, coasterConfig, trackDescriptions);
        return trackFactory.createTrack();
    }

    private TrackDescription loadTrackSegmentFromConfig(String trackIdentifier, CoasterHandle coasterHandle, CoasterConfig coasterConfig, float offsetX, float offsetY, float offsetZ){
        Ride ride = coasterHandle.getRide();
        String rideIdentifier = ride.getIdentifier();
        String configFileName = configManager.getFolder(rideIdentifier) + "/track/" + rideIdentifier + "." + trackIdentifier + ".csv";
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

        Frame startFrame = new SimpleFrame(0);
        Frame endFrame = new SimpleFrame(positions.size()-1);
        return new TrackDescription(trackIdentifier, positions, TrackType.TRACK, startFrame, endFrame);
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
