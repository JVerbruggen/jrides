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

package com.jverbruggen.jrides.config.coaster.objects.section.base;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.*;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer.TransferSectionSpecConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.section.reference.RangedSectionReference;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RangedSectionConfig extends SectionConfig {
    private final int lowerRange;
    private final int upperRange;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;
    private final boolean forwards;
    private final String trackSource;
    private final String nextSection;
    private final String arrivalUnlocksSection;
    private final List<String> conflictSections;
    private final BlockSectionSpecConfig blockSectionSpec;
    private final CoasterStationConfig stationSectionSpec;
    private final BrakeSectionSpecConfig brakeSectionSpec;
    private final TrimBrakeSectionSpecConfig trimBrakeSectionSpecConfig;
    private final DriveSectionSpecConfig driveSectionSpec;
    private final ProximityDriveSectionSpecConfig proximityDriveSectionSpecConfig;
    private final DriveAndReleaseSectionSpecConfig driveAndReleaseSectionSpecConfig;
    private final DriveStopDriveSectionSpecConfig driveStopDriveSectionSpecConfig;
    private final StorageSectionSpecConfig storageSectionSpec;
    private final TransferSectionSpecConfig transferSectionSpec;
    private final LaunchSectionSpecConfig launchSectionSpecConfig;

    public RangedSectionConfig(String identifier, int lowerRange, int upperRange, boolean jumpAtStart, boolean jumpAtEnd, boolean forwards, String trackSource, String type,
                               String nextSection, String arrivalUnlocksSection, List<String> conflictSections,
                               BlockSectionSpecConfig blockSectionSpec, CoasterStationConfig stationSectionSpec, BrakeSectionSpecConfig brakeSectionSpec,
                               TrimBrakeSectionSpecConfig trimBrakeSectionSpecConfig, DriveSectionSpecConfig driveSectionSpec, ProximityDriveSectionSpecConfig proximityDriveSectionSpecConfig, StorageSectionSpecConfig storageSectionSpec, TransferSectionSpecConfig transferSectionSpec,
                               LaunchSectionSpecConfig launchSectionSpecConfig, DriveAndReleaseSectionSpecConfig driveAndReleaseSectionSpecConfig, DriveStopDriveSectionSpecConfig driveStopDriveSectionSpecConfig) {
        super(type, identifier);
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.forwards = forwards;
        this.trackSource = trackSource;
        this.nextSection = nextSection;
        this.arrivalUnlocksSection = arrivalUnlocksSection;
        this.conflictSections = conflictSections;
        this.blockSectionSpec = blockSectionSpec;
        this.stationSectionSpec = stationSectionSpec;
        this.brakeSectionSpec = brakeSectionSpec;
        this.trimBrakeSectionSpecConfig = trimBrakeSectionSpecConfig;
        this.driveSectionSpec = driveSectionSpec;
        this.proximityDriveSectionSpecConfig = proximityDriveSectionSpecConfig;
        this.driveAndReleaseSectionSpecConfig = driveAndReleaseSectionSpecConfig;
        this.storageSectionSpec = storageSectionSpec;
        this.transferSectionSpec = transferSectionSpec;
        this.launchSectionSpecConfig = launchSectionSpecConfig;
        this.driveStopDriveSectionSpecConfig = driveStopDriveSectionSpecConfig;
    }

    public String getNextSection() {
        return nextSection;
    }

    public List<String> getConflictSections() {
        return conflictSections;
    }

    public String getArrivalUnlocksSection() {
        return arrivalUnlocksSection;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public String getParentTrackIdentifier() {
        return trackSource;
    }

    public BlockSectionSpecConfig getBlockSectionSpec() {
        return blockSectionSpec;
    }

    public CoasterStationConfig getStationSectionSpec() {
        return stationSectionSpec;
    }

    public BrakeSectionSpecConfig getBrakeSectionSpec() {
        return brakeSectionSpec;
    }

    public TrimBrakeSectionSpecConfig getTrimBrakeSectionSpecConfig() {
        return trimBrakeSectionSpecConfig;
    }

    public DriveSectionSpecConfig getDriveSectionSpec() {
        return driveSectionSpec;
    }

    public ProximityDriveSectionSpecConfig getProximityDriveSectionSpec() {
        return proximityDriveSectionSpecConfig;
    }

    public DriveAndReleaseSectionSpecConfig getDriveAndReleaseSectionSpecConfig() {
        return driveAndReleaseSectionSpecConfig;
    }

    public DriveStopDriveSectionSpecConfig getDriveStopDriveSectionSpecConfig() {
        return driveStopDriveSectionSpecConfig;
    }

    public StorageSectionSpecConfig getStorageSectionSpec() {
        return storageSectionSpec;
    }

    public TransferSectionSpecConfig getTransferSectionSpec() {
        return transferSectionSpec;
    }

    public LaunchSectionSpecConfig getLaunchSectionSpecConfig() {
        return launchSectionSpecConfig;
    }

    public boolean isJumpAtStart() {
        return jumpAtStart;
    }

    public boolean isJumpAtEnd() {
        return jumpAtEnd;
    }

    public boolean isForwards() {
        return forwards;
    }

    public static boolean accepts(String type){
        return switch (type) {
            case "track", "drive", "proximityDrive", "driveAndRelease", "driveStopDrive", "brake", "trim", "station", "blocksection", "transfer", "launch" -> true;
            default -> false;
        };
    }

    public static RangedSectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        String type = getString(configurationSection, "type");
        List<?> range = configurationSection.getList("range");

        int lowerRange = (Integer)range.get(0);
        int upperRange = (Integer)range.get(1);
        String trackSource = "default";
        if(range.size() == 3)
            trackSource = (String)range.get(2);
        String nextSection = getString(configurationSection, "nextSection");
        String arrivalUnlocks = getString(configurationSection, "arrivalUnlocks", null);

        List<String> conflictSections = getStringList(configurationSection, "conflictSections", null);

        boolean jumpAtStart = getBoolean(configurationSection, "jumpAtStart", false);
        boolean jumpAtEnd = getBoolean(configurationSection, "jumpAtEnd", false);
        boolean forwards = getBoolean(configurationSection, "forwards", true);

        BlockSectionSpecConfig blockSectionSpec = null;
        if(configurationSection.contains("blockSection"))
            blockSectionSpec = BlockSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("blockSection"));

        CoasterStationConfig stationSectionSpec = null;
        if(configurationSection.contains("stationSection"))
            stationSectionSpec = CoasterStationConfig.fromConfigurationSection(configurationSection.getConfigurationSection("stationSection"));

        BrakeSectionSpecConfig brakeSectionSpec = null;
        if(configurationSection.contains("brakeSection"))
            brakeSectionSpec = BrakeSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("brakeSection"));

        TrimBrakeSectionSpecConfig trimBrakeSectionSpecConfig = null;
        if(type.equals("trim"))
            trimBrakeSectionSpecConfig = TrimBrakeSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("trimSection"));

        DriveSectionSpecConfig driveSectionSpec = null;
        if(type.equals("drive"))
            driveSectionSpec = DriveSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("driveSection"));

        ProximityDriveSectionSpecConfig proximityDriveSectionSpecConfig = null;
        if(type.equals("proximityDrive"))
            proximityDriveSectionSpecConfig = ProximityDriveSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("proximityDriveSection"));

        DriveAndReleaseSectionSpecConfig driveAndReleaseSectionSpec = null;
        if(type.equals("driveAndRelease"))
            driveAndReleaseSectionSpec = DriveAndReleaseSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("driveAndReleaseSection"));

        DriveStopDriveSectionSpecConfig driveStopDriveSectionSpecConfig = null;
        if(type.equals("driveStopDrive"))
            driveStopDriveSectionSpecConfig = DriveStopDriveSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("driveStopDriveSection"));

        StorageSectionSpecConfig storageSectionSpec = null;
        if(configurationSection.contains("storageSection"))
            storageSectionSpec = StorageSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("storageSection"));

        TransferSectionSpecConfig transferSectionSpec = null;
        if(configurationSection.contains("transferSection"))
            transferSectionSpec = TransferSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("transferSection"));

        LaunchSectionSpecConfig launchSectionSpec = null;
        if(configurationSection.contains("launchSection"))
            launchSectionSpec = LaunchSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("launchSection"));

        return new RangedSectionConfig(sectionIdentifier, lowerRange, upperRange, jumpAtStart, jumpAtEnd, forwards, trackSource, type, nextSection, arrivalUnlocks, conflictSections,
                blockSectionSpec, stationSectionSpec, brakeSectionSpec, trimBrakeSectionSpecConfig, driveSectionSpec, proximityDriveSectionSpecConfig, storageSectionSpec, transferSectionSpec, launchSectionSpec,
                driveAndReleaseSectionSpec, driveStopDriveSectionSpecConfig);
    }

    @Override
    public SectionReference build(TrackBehaviourFactory trackBehaviourFactory, List<TrackDescription> trackDescriptions, CoasterHandle coasterHandle,
                                  CoasterConfig coasterConfig) {
        String sectionIdentifier = getIdentifier();
        String nextSectionIdentifier = getNextSection();
        String parentTrackIdentifier = getParentTrackIdentifier();
        String arrivalUnlocks = getArrivalUnlocksSection();

        boolean jumpAtStart = isJumpAtStart();
        boolean jumpAtEnd = isJumpAtEnd();
        boolean forwards = isForwards();

        TrackDescription trackDescription = trackDescriptions.stream()
                .filter(d -> d.getIdentifier().equalsIgnoreCase(getParentTrackIdentifier()))
                .findFirst().orElseThrow();

        Frame startFrame = new SimpleFrame(getLowerRange());
        Frame endFrame = new SimpleFrame(getUpperRange());

        TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, this, trackDescription.getCycle());
        if(trackBehaviour == null) return null;

        return new RangedSectionReference(sectionIdentifier, startFrame, endFrame, trackBehaviour, nextSectionIdentifier, conflictSections, parentTrackIdentifier,
                arrivalUnlocks, jumpAtStart, jumpAtEnd, forwards);
    }
}

