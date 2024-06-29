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

package com.jverbruggen.jrides.generate;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TemplateGenerator {
    public void generate(GenerateType generateType, String identifier){
        switch (generateType){
            case COASTER -> generateCoaster(identifier);
            case FLAT_RIDE -> generateFlatRide(identifier);
        }

        ServiceProvider.getSingleton(RideManager.class).addUnloadedReference(identifier);
    }

    private File getDataFolder(){
        return JRidesPlugin.getBukkitPlugin().getDataFolder();
    }

    private void createFoldersIfNotExists(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private void createNewFileIfNotExists(File file){
        if(!file.exists()){
            boolean created = false;
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(!created){
                throw new RuntimeException("File '" + file.getName() + "' could not be created because it already exists.");
            }
        }
    }

    private void addToRidesYml(String rideIdentifier, GenerateType generateType){
        File dataFolder = getDataFolder();

        File ridesYmlFile = new File(dataFolder, "rides.yml");
        createNewFileIfNotExists(ridesYmlFile);

        YamlConfiguration ridesYmlConfiguration = YamlConfiguration.loadConfiguration(ridesYmlFile);
        ridesYmlConfiguration.set("config." + rideIdentifier + ".identifier", rideIdentifier);
        ridesYmlConfiguration.set("config." + rideIdentifier + ".type", GenerateType.toString(generateType));
        try {
            ridesYmlConfiguration.save(ridesYmlFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillCommonYmlTags(YamlConfiguration yamlConfiguration, String identifier){
        yamlConfiguration.set("config.manifestVersion", "v1");
        yamlConfiguration.set("config.identifier", identifier);
        yamlConfiguration.set("config.displayName", "Template for " + identifier);
        yamlConfiguration.set("config.displayItem.material", "DIRT");
        yamlConfiguration.set("config.canExitDuringRide", false);
    }

    public void generateCoaster(String identifier){
        File dataFolder = getDataFolder();

        addToRidesYml(identifier, GenerateType.COASTER);

        File identifierFolder = new File(dataFolder, "coasters/" + identifier);
        createFoldersIfNotExists(identifierFolder);

        File coasterYmlFile = new File(identifierFolder, identifier + ".coaster.yml");
        createNewFileIfNotExists(coasterYmlFile);

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(coasterYmlFile);
        fillCommonYmlTags(yamlConfiguration, identifier);
        yamlConfiguration.set("config.vehicles.trains", 1);
        yamlConfiguration.set("config.vehicles.carts", 1);
        yamlConfiguration.set("config.cartSpec.default.model.item.material", "DIRT");

        try {
            yamlConfiguration.save(coasterYmlFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateFlatRide(String identifier){
        File dataFolder = getDataFolder();

        addToRidesYml(identifier, GenerateType.FLAT_RIDE);

        File identifierFolder = new File(dataFolder, "flatrides/" + identifier);
        createFoldersIfNotExists(identifierFolder);

        File coasterYmlFile = new File(identifierFolder, identifier + ".flatride.yml");
        createNewFileIfNotExists(coasterYmlFile);

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(coasterYmlFile);
        fillCommonYmlTags(yamlConfiguration, identifier);

        try {
            yamlConfiguration.save(coasterYmlFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
