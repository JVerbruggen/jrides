package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.*;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class CoasterConfig extends BaseConfig {
    private final String manifestVersion;
    private final String identifier;
    private final String displayName;
    private final PlayerLocation warpLocation;
    private final TrackConfig track;
    private final VehiclesConfig vehicles;
    private final CartSpecConfig cartSpec;
    private final GatesConfig gates;
    private final double gravityConstant;
    private final double dragConstant;
    private final ControllerConfig controllerConfig;
    private final SoundsConfig soundsConfig;
    private final int rideOverviewMapId;
    private final boolean canExitDuringRide;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, PlayerLocation warpLocation, TrackConfig track,
                         VehiclesConfig vehicles, CartSpecConfig cartSpec, GatesConfig gates, double gravityConstant, double dragConstant,
                         ControllerConfig controllerConfig, SoundsConfig soundsConfig, int rideOverviewMapId, boolean canExitDuringRide) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.warpLocation = warpLocation;
        this.track = track;
        this.vehicles = vehicles;
        this.cartSpec = cartSpec;
        this.gates = gates;
        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
        this.controllerConfig = controllerConfig;
        this.soundsConfig = soundsConfig;
        this.rideOverviewMapId = rideOverviewMapId;
        this.canExitDuringRide = canExitDuringRide;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }

    public TrackConfig getTrack() {
        return track;
    }

    public VehiclesConfig getVehicles() {
        return vehicles;
    }

    public CartSpecConfig getCartSpec() {
        return cartSpec;
    }

    public GatesConfig getGates() {
        return gates;
    }

    public double getGravityConstant() {
        return gravityConstant;
    }

    public double getDragConstant() {
        return dragConstant;
    }

    public ControllerConfig getControllerConfig() {
        return controllerConfig;
    }

    public SoundsConfig getSoundsConfig() {
        return soundsConfig;
    }

    public int getRideOverviewMapId() {
        return rideOverviewMapId;
    }

    public boolean getCanExitDuringRide() {
        return canExitDuringRide;
    }

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");
        PlayerLocation warpLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        double gravityConstant = getDouble(configurationSection, "gravityConstant", 0.15);
        double dragConstant = getDouble(configurationSection, "dragConstant", 0.9993);
        TrackConfig track = TrackConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("track")));
        VehiclesConfig vehicles = VehiclesConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("vehicles")));
        CartSpecConfig cartSpec = CartSpecConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("cartSpec")));
        GatesConfig gates = GatesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("gates"));
        SoundsConfig sounds = SoundsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("sounds"));
        ControllerConfig controllerConfig = ControllerConfig.fromConfigurationSection(configurationSection.getConfigurationSection("controller"));
        int rideOverviewMapId = getInt(configurationSection, "rideOverviewMapId", -1);
        boolean canExitDuringRide = getBoolean(configurationSection, "canExitDuringRide", false);

        return new CoasterConfig(manifestVersion, identifier, displayName, warpLocation, track, vehicles,
                cartSpec, gates, gravityConstant, dragConstant, controllerConfig, sounds, rideOverviewMapId,
                canExitDuringRide);
    }
}
