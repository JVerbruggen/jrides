package com.jverbruggen.jrides.state.ride.flatride;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AnimationLoader {
    private final File dataFolder;
    private final ConfigManager configManager;
    private final HashMap<String, AnimationHandle> cachedFlatRideAnimationHandles;

    public AnimationLoader(File dataFolder) {
        this.dataFolder = dataFolder;
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.cachedFlatRideAnimationHandles = new LinkedHashMap<>();
    }

    public AnimationHandle loadFlatRideAnimation(String animationHandleIdentifier, FlatRideHandle flatRideHandle){
        if(cachedFlatRideAnimationHandles.containsKey(animationHandleIdentifier))
            return cachedFlatRideAnimationHandles.get(animationHandleIdentifier);

        Ride ride = flatRideHandle.getRide();
        String rideIdentifier = ride.getIdentifier();
        String configFileName = configManager.getFlatrideFolder(rideIdentifier) + "/animations/" + rideIdentifier + "." + animationHandleIdentifier + ".csv";
        File configFile = new File(dataFolder, configFileName);
        Path pathToConfigFile = configFile.toPath();
        List<BlenderExportPositionRecord> positions = new ArrayList<>();

        try(BufferedReader br = Files.newBufferedReader(pathToConfigFile, StandardCharsets.UTF_8)){
            String line = br.readLine(); // Skip header line
            line = br.readLine();
            int index = 0;
            while(line != null){
                String[] attributes = line.split(", ");
                BlenderExportPositionRecord record = BlenderExportPositionRecord.createFromCSVAttributes(attributes);
                positions.add(record);

                line = br.readLine();
                index++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        AnimationHandle animationHandle = AnimationHandle.createAnimationHandle(positions);
        cachedFlatRideAnimationHandles.put(animationHandleIdentifier, animationHandle);

        return animationHandle;
    }
}
