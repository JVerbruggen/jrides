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
    private final String trackSource;
    private final String nextSection;
    private final List<String> conflictSections;
    private final BlockSectionSpecConfig blockSectionSpec;
    private final CoasterStationConfig stationSectionSpec;
    private final BrakeSectionSpecConfig brakeSectionSpec;
    private final DriveSectionSpecConfig driveSectionSpec;
    private final StorageSectionSpecConfig storageSectionSpec;
    private final TransferSectionSpecConfig transferSectionSpec;
    private final LaunchSectionSpecConfig launchSectionSpecConfig;

    public RangedSectionConfig(String identifier, int lowerRange, int upperRange, boolean jumpAtStart, boolean jumpAtEnd, String trackSource, String type,
                               String nextSection, List<String> conflictSections,
                               BlockSectionSpecConfig blockSectionSpec, CoasterStationConfig stationSectionSpec, BrakeSectionSpecConfig brakeSectionSpec,
                               DriveSectionSpecConfig driveSectionSpec, StorageSectionSpecConfig storageSectionSpec, TransferSectionSpecConfig transferSectionSpec,
                               LaunchSectionSpecConfig launchSectionSpecConfig) {
        super(type, identifier);
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.trackSource = trackSource;
        this.nextSection = nextSection;
        this.conflictSections = conflictSections;
        this.blockSectionSpec = blockSectionSpec;
        this.stationSectionSpec = stationSectionSpec;
        this.brakeSectionSpec = brakeSectionSpec;
        this.driveSectionSpec = driveSectionSpec;
        this.storageSectionSpec = storageSectionSpec;
        this.transferSectionSpec = transferSectionSpec;
        this.launchSectionSpecConfig = launchSectionSpecConfig;
    }

    public String getNextSection() {
        return nextSection;
    }

    public List<String> getConflictSections() {
        return conflictSections;
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

    public DriveSectionSpecConfig getDriveSectionSpec() {
        return driveSectionSpec;
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

    public static boolean accepts(String type){
        return switch (type) {
            case "track", "drive", "brake", "station", "blocksection", "transfer", "launch" -> true;
            default -> false;
        };
    }

    public static RangedSectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        String type = configurationSection.getString("type");
        List<?> range = configurationSection.getList("range");

        int lowerRange = (Integer)range.get(0);
        int upperRange = (Integer)range.get(1);
        String trackSource = "default";
        if(range.size() == 3)
            trackSource = (String)range.get(2);
        String nextSection = configurationSection.getString("nextSection");

        List<String> conflictSections = getStringList(configurationSection, "conflictSections", null);

        boolean jumpAtStart = getBoolean(configurationSection, "jumpAtStart", false);
        boolean jumpAtEnd = getBoolean(configurationSection, "jumpAtEnd", false);

        BlockSectionSpecConfig blockSectionSpec = null;
        if(configurationSection.contains("blockSection"))
            blockSectionSpec = BlockSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("blockSection"));

        CoasterStationConfig stationSectionSpec = null;
        if(configurationSection.contains("stationSection"))
            stationSectionSpec = CoasterStationConfig.fromConfigurationSection(configurationSection.getConfigurationSection("stationSection"));

        BrakeSectionSpecConfig brakeSectionSpec = null;
        if(configurationSection.contains("brakeSection"))
            brakeSectionSpec = BrakeSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("brakeSection"));

        DriveSectionSpecConfig driveSectionSpec = null;
        if(configurationSection.contains("driveSection"))
            driveSectionSpec = DriveSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("driveSection"));

        StorageSectionSpecConfig storageSectionSpec = null;
        if(configurationSection.contains("storageSection"))
            storageSectionSpec = StorageSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("storageSection"));

        TransferSectionSpecConfig transferSectionSpec = null;
        if(configurationSection.contains("transferSection"))
            transferSectionSpec = TransferSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("transferSection"));

        LaunchSectionSpecConfig launchSectionSpec = null;
        if(configurationSection.contains("launchSection"))
            launchSectionSpec = LaunchSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("launchSection"));

        return new RangedSectionConfig(sectionIdentifier, lowerRange, upperRange, jumpAtStart, jumpAtEnd, trackSource, type, nextSection, conflictSections,
                blockSectionSpec, stationSectionSpec, brakeSectionSpec, driveSectionSpec, storageSectionSpec, transferSectionSpec, launchSectionSpec);
    }

    @Override
    public SectionReference build(TrackBehaviourFactory trackBehaviourFactory, List<TrackDescription> trackDescriptions, CoasterHandle coasterHandle,
                                  CoasterConfig coasterConfig) {
        String sectionIdentifier = getIdentifier();
        String nextSectionIdentifier = getNextSection();
        String parentTrackIdentifier = getParentTrackIdentifier();

        boolean jumpAtStart = isJumpAtStart();
        boolean jumpAtEnd = isJumpAtEnd();

        TrackDescription trackDescription = trackDescriptions.stream()
                .filter(d -> d.getIdentifier().equalsIgnoreCase(getParentTrackIdentifier()))
                .findFirst().orElseThrow();

        Frame startFrame = new SimpleFrame(getLowerRange());
        Frame endFrame = new SimpleFrame(getUpperRange());

        TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, this, trackDescription.getCycle());
        if(trackBehaviour == null) return null;

        return new RangedSectionReference(sectionIdentifier, startFrame, endFrame, trackBehaviour, nextSectionIdentifier, conflictSections, parentTrackIdentifier,
                jumpAtStart, jumpAtEnd);
    }
}

