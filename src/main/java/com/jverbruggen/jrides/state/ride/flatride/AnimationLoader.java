/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.state.ride.flatride;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
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
    private final HashMap<String, AnimationHandle> cachedCoasterEffectAnimationHandles;

    public AnimationLoader(File dataFolder) {
        this.dataFolder = dataFolder;
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.cachedFlatRideAnimationHandles = new HashMap<>();
        this.cachedCoasterEffectAnimationHandles = new HashMap<>();
    }

    private AnimationHandle loadAnimation(String configFileName){
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

        return AnimationHandle.createAnimationHandle(positions);
    }

    public AnimationHandle loadFlatRideAnimation(String animationHandleIdentifier, FlatRideHandle flatRideHandle){
        if(cachedFlatRideAnimationHandles.containsKey(animationHandleIdentifier))
            return cachedFlatRideAnimationHandles.get(animationHandleIdentifier);

        Ride ride = flatRideHandle.getRide();
        String rideIdentifier = ride.getIdentifier();
        String configFileName = configManager.getFlatrideFolder(rideIdentifier) + "/animations/" + rideIdentifier + "." + animationHandleIdentifier + ".csv";

        AnimationHandle animationHandle = loadAnimation(configFileName);
        cachedFlatRideAnimationHandles.put(animationHandleIdentifier, animationHandle);

        return animationHandle;
    }

    public AnimationHandle loadCoasterEffectAnimation(String animationHandleIdentifier, String rideIdentifier){
        if(cachedCoasterEffectAnimationHandles.containsKey(animationHandleIdentifier))
            return cachedCoasterEffectAnimationHandles.get(animationHandleIdentifier);

        String configFileName = configManager.getCoasterFolder(rideIdentifier) + "/animations/" + rideIdentifier + "." + animationHandleIdentifier + ".csv";

        AnimationHandle animationHandle = loadAnimation(configFileName);
        cachedCoasterEffectAnimationHandles.put(animationHandleIdentifier, animationHandle);

        return animationHandle;
    }
}
