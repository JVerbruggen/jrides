package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.section.transfer.TransferSectionSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SectionConfig {
    private final String identifier;
    private final int lowerRange;
    private final int upperRange;
    private final String trackSource;
    private final String type;
    private final BlockSectionSpecConfig blockSectionSpec;
    private final StationSpecConfig stationSectionSpec;
    private final BrakeSectionSpecConfig brakeSectionSpec;
    private final DriveSectionSpecConfig driveSectionSpec;
    private final StorageSectionSpecConfig storageSectionSpec;
    private final TransferSectionSpecConfig transferSectionSpec;

    public SectionConfig(String identifier, int lowerRange, int upperRange, String trackSource, String type, BlockSectionSpecConfig blockSectionSpec, StationSpecConfig stationSectionSpec, BrakeSectionSpecConfig brakeSectionSpec, DriveSectionSpecConfig driveSectionSpec, StorageSectionSpecConfig storageSectionSpec, TransferSectionSpecConfig transferSectionSpec) {
        this.identifier = identifier;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.trackSource = trackSource;
        this.type = type;
        this.blockSectionSpec = blockSectionSpec;
        this.stationSectionSpec = stationSectionSpec;
        this.brakeSectionSpec = brakeSectionSpec;
        this.driveSectionSpec = driveSectionSpec;
        this.storageSectionSpec = storageSectionSpec;
        this.transferSectionSpec = transferSectionSpec;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public String getTrackSource() {
        return trackSource;
    }

    public String getType() {
        return type;
    }

    public BlockSectionSpecConfig getBlockSectionSpec() {
        return blockSectionSpec;
    }

    public StationSpecConfig getStationSectionSpec() {
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

    public static SectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        List<?> range = configurationSection.getList("range");

        int lowerRange = (Integer)range.get(0);
        int upperRange = (Integer)range.get(1);
        String trackSource = "default";
        if(range.size() == 3)
            trackSource = (String)range.get(2);
        String type = configurationSection.getString("type");

        BlockSectionSpecConfig blockSectionSpec = null;
        if(configurationSection.contains("blockSection"))
            blockSectionSpec = BlockSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("blockSection"));

        StationSpecConfig stationSectionSpec = null;
        if(configurationSection.contains("stationSection"))
            stationSectionSpec = StationSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("stationSection"));

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

        return new SectionConfig(sectionIdentifier, lowerRange, upperRange, trackSource, type, blockSectionSpec, stationSectionSpec, brakeSectionSpec, driveSectionSpec, storageSectionSpec, transferSectionSpec);
    }
}
