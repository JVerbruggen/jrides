package com.jverbruggen.jrides.state.ride.coaster;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.controller.RideControllerFactory;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.exception.CoasterLoadException;
import com.jverbruggen.jrides.exception.NoTrackSpecifiedException;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.coaster.SimpleCoaster;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.factory.ConfigAdvancedSplineTrackFactory;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import com.jverbruggen.jrides.models.ride.factory.TrackFactory;
import com.jverbruggen.jrides.models.ride.factory.TrainFactory;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.factory.train.TrainCreationResult;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoasterLoader {
    private final JRidesLogger logger;
    private final File dataFolder;
    private final ConfigManager configManager;
    private final TrainFactory trainFactory;
    private final SeatFactory seatFactory;
    private final EffectTriggerFactory effectTriggerFactory;
    private final RideControllerFactory rideControllerFactory;
    private final RideControlMenuFactory rideControlMenuFactory;

    public CoasterLoader(File dataFolder) {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.dataFolder = dataFolder;
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.trainFactory = ServiceProvider.getSingleton(TrainFactory.class);
        this.seatFactory = ServiceProvider.getSingleton(SeatFactory.class);
        this.effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);
        this.rideControllerFactory = ServiceProvider.getSingleton(RideControllerFactory.class);
        this.rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
    }

    public CoasterHandle loadCoaster(CoasterConfig coasterConfig, String rideIdentifier, RideState rideState, World world) throws CoasterLoadException {
        CoasterHandle coasterHandle = null;
        boolean tryAgain = true;
        int tries = 5;

        while(tryAgain && tries > 0){
            tryAgain = false;
            tries--;
            try{
                coasterHandle = loadNormally(coasterConfig, rideIdentifier, rideState, world);
            }catch (NoTrackSpecifiedException exception){
                if(rideState.isInactive()) throw exception;
                rideState.setInactive();
                tryAgain = true;
            }
        }

        return coasterHandle;
    }

    public CoasterHandle loadNormally(CoasterConfig coasterConfig, String rideIdentifier, RideState rideState, World world) throws CoasterLoadException {
        String displayName = coasterConfig.getDisplayName();
        List<String> displayDescription = Arrays.stream(coasterConfig.getDisplayDescription().split("\\\\n"))
                .map(d -> ChatColor.GRAY + d)
                .collect(Collectors.toList());
        if(displayDescription.size() == 1 && ChatColor.stripColor(displayDescription.get(0)).equals(""))
            displayDescription.clear();

        ItemStack displayItem = ItemStackFactory.getCoasterStackFromConfig(coasterConfig.getDisplayItem());

        PlayerLocation warpLocation = coasterConfig.getWarpLocation();
        boolean canExitDuringRide = coasterConfig.getCanExitDuringRide();
        Ride ride = new SimpleCoaster(rideIdentifier, displayName, displayDescription, displayItem, warpLocation, canExitDuringRide);

        // Initialize Handle
        int rideOverviewMapId = coasterConfig.getRideOverviewMapId();
        SoundsConfig sounds = coasterConfig.getSoundsConfig();
        String dispatchSound = sounds.getDispatch();
        String restraintOpenSound = sounds.getRestraintOpen();
        String restraintCloseSound = sounds.getRestraintClose();
        String windSound = sounds.getOnrideWind();

        CoasterHandle coasterHandle = new CoasterHandle(ride, world, dispatchSound, restraintOpenSound,
                restraintCloseSound, windSound, rideOverviewMapId, !rideState.isInactive());

        // Initialize Track
        if(!rideState.isInactive()){
            TrackConfig trackConfig = coasterConfig.getTrack();
            if(!trackConfig.isCorrectlyLoaded()){
                throw new NoTrackSpecifiedException("Not correctly configured in coaster config");
            }
            List<Float> offset = trackConfig.getPosition();

            float offsetX = offset.get(0);
            float offsetY = offset.get(1);
            float offsetZ = offset.get(2);
            Track track = loadCoasterTrackFromConfig(coasterHandle, coasterConfig, offsetX, offsetY, offsetZ);
            if(track == null){
                logger.severe("Track was null, so coaster " + rideIdentifier + " could not be loaded");
                throw new NoTrackSpecifiedException();
            }
            coasterHandle.setTrack(track);

            EffectTriggerCollection<TrainEffectTriggerHandle> effectTriggerCollection = effectTriggerFactory.getTrainEffectTriggers(rideIdentifier, track);
            coasterHandle.setEffectTriggerCollection(effectTriggerCollection);

            SectionProvider sectionProvider = new SectionProvider();
            int trainCount = coasterConfig.getVehicles().getTrains();

            TrainCreationResult trainCreationResult = createTrains(coasterHandle, track, coasterConfig, sectionProvider, rideIdentifier, trainCount);
            List<TrainHandle> trainHandles = trainCreationResult.output();
            if(!trainCreationResult.success()){
                coasterHandle.unload(false);
                trainFactory.unloadAll(trainHandles);
                throw new CoasterLoadException();
            }
            coasterHandle.setTrains(trainHandles);

            RideController rideController = rideControllerFactory.createRideController(coasterHandle, coasterConfig.getControllerConfig());
            Menu rideControlMenu = rideControlMenuFactory.getRideControlMenu(rideController, coasterConfig.getControllerConfig());
            coasterHandle.setRideController(rideController, rideControlMenu);
        }

        coasterHandle.setState(rideState);

        return coasterHandle;
    }

    private TrainCreationResult createTrains(CoasterHandle coasterHandle, Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String rideIdentifier, int count) {
        List<TrainHandle> trains = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String trainName = rideIdentifier + ":train_" + (i+1);
            TrainHandle trainHandle = createTrain(coasterHandle, track, coasterConfig, sectionProvider, trainName);
            if(trainHandle != null)
                trains.add(trainHandle);
            else return new TrainCreationResult(trains, false);
        }
        return new TrainCreationResult(trains, true);
    }

    private Track loadCoasterTrackFromConfig(CoasterHandle coasterHandle, CoasterConfig coasterConfig, float offsetX, float offsetY, float offsetZ){
        List<String> trackIdentifiers = coasterConfig.getTrack().getParts();
        List<TrackDescription> trackDescriptions = trackIdentifiers.stream()
                .map(identifier -> loadTrackSegmentFromConfig(identifier, coasterHandle, coasterConfig, offsetX, offsetY, offsetZ))
                .collect(Collectors.toList());

        TrackFactory trackFactory = new ConfigAdvancedSplineTrackFactory(coasterHandle, coasterConfig, trackDescriptions);
        return trackFactory.createTrack();
    }

    private TrainHandle createTrain(CoasterHandle coasterHandle, Track track, CoasterConfig coasterConfig, SectionProvider sectionProvider, String trainIdentifier){
        Train train = trainFactory.createEquallyDistributedTrain(coasterHandle, track, coasterConfig, trainIdentifier);
        if(train == null) return null;

        return new TrainHandle(sectionProvider, train, track);
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
}
