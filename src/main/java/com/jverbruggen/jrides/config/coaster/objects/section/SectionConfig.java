package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.connection.ConnectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.connection.ConnectionsConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.transfer.TransferSectionSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SectionConfig extends BaseConfig {
    private final String identifier;
    private final int lowerRange;
    private final int upperRange;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;
    private final String trackSource;
    private final String type;
    private final String nextSection;
    private final ConnectionsConfig connectionsConfig;
    private final BlockSectionSpecConfig blockSectionSpec;
    private final StationSpecConfig stationSectionSpec;
    private final BrakeSectionSpecConfig brakeSectionSpec;
    private final DriveSectionSpecConfig driveSectionSpec;
    private final StorageSectionSpecConfig storageSectionSpec;
    private final TransferSectionSpecConfig transferSectionSpec;
    private final LaunchSectionSpecConfig launchSectionSpecConfig;

    public SectionConfig(String identifier, int lowerRange, int upperRange, boolean jumpAtStart, boolean jumpAtEnd, String trackSource, String type,
                         String nextSection, ConnectionsConfig connectionsConfig,
                         BlockSectionSpecConfig blockSectionSpec, StationSpecConfig stationSectionSpec, BrakeSectionSpecConfig brakeSectionSpec,
                         DriveSectionSpecConfig driveSectionSpec, StorageSectionSpecConfig storageSectionSpec, TransferSectionSpecConfig transferSectionSpec,
                         LaunchSectionSpecConfig launchSectionSpecConfig) {
        this.identifier = identifier;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.trackSource = trackSource;
        this.type = type;
        this.nextSection = nextSection;
        this.connectionsConfig = connectionsConfig;
        this.blockSectionSpec = blockSectionSpec;
        this.stationSectionSpec = stationSectionSpec;
        this.brakeSectionSpec = brakeSectionSpec;
        this.driveSectionSpec = driveSectionSpec;
        this.storageSectionSpec = storageSectionSpec;
        this.transferSectionSpec = transferSectionSpec;
        this.launchSectionSpecConfig = launchSectionSpecConfig;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNextSection() {
        return nextSection;
    }

    public ConnectionsConfig getConnectionsConfig() {
        return connectionsConfig;
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

    public LaunchSectionSpecConfig getLaunchSectionSpecConfig() {
        return launchSectionSpecConfig;
    }

    public boolean isJumpAtStart() {
        return jumpAtStart;
    }

    public boolean isJumpAtEnd() {
        return jumpAtEnd;
    }

    public static SectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        List<?> range = configurationSection.getList("range");

        int lowerRange = (Integer)range.get(0);
        int upperRange = (Integer)range.get(1);
        String trackSource = "default";
        if(range.size() == 3)
            trackSource = (String)range.get(2);
        String type = configurationSection.getString("type");
        String nextSection = configurationSection.getString("nextSection");

        boolean jumpAtStart = getBoolean(configurationSection, "jumpAtStart", false);
        boolean jumpAtEnd = getBoolean(configurationSection, "jumpAtEnd", false);
        ConnectionsConfig connectionsConfig = getConnections(configurationSection);

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

        LaunchSectionSpecConfig launchSectionSpec = null;
        if(configurationSection.contains("launchSection"))
            launchSectionSpec = LaunchSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("launchSection"));

        return new SectionConfig(sectionIdentifier, lowerRange, upperRange, jumpAtStart, jumpAtEnd, trackSource, type, nextSection, connectionsConfig, blockSectionSpec, stationSectionSpec, brakeSectionSpec, driveSectionSpec, storageSectionSpec, transferSectionSpec, launchSectionSpec);
    }

    private static ConnectionsConfig getConnections(ConfigurationSection configurationSection){
        if(configurationSection == null) return new ConnectionsConfig();
        List<?> connections = configurationSection.getList("connections");
        if(connections == null) return new ConnectionsConfig();

        Object rawStartConnection = connections.get(0);
        Object rawEndConnection = connections.get(1);

        ConnectionSpecConfig startConnection = getConnectionSpec(rawStartConnection);
        ConnectionSpecConfig endConnection = getConnectionSpec(rawEndConnection);

        return new ConnectionsConfig(startConnection, endConnection);
    }

    private static ConnectionSpecConfig getConnectionSpec(Object rawConnection){
        if(rawConnection == null) return null;
        if(!(rawConnection instanceof List))
            throw new RuntimeException("Unexpected connection specification: " + rawConnection);

        List<?> connectionDetails = (List<?>) rawConnection;
        if(connectionDetails.size() != 2)
            throw new RuntimeException("Expected exactly two arguments for connection specification, but got different");

        int connectionFrame = (Integer) connectionDetails.get(0);
        String connectionTrack = (String) connectionDetails.get(1);

        return new ConnectionSpecConfig(connectionFrame, connectionTrack);
    }
}

