package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.CartSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.VehiclesConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class CoasterConfig {
    private final String manifestVersion;
    private final String identifier;
    private final String displayName;
    private final Vector3 warpLocation;
    private final TrackConfig track;
    private final VehiclesConfig vehicles;
    private final CartSpecConfig cartSpec;
    private final GatesConfig gates;
    private final double gravityConstant;
    private final double dragConstant;
    private final SoundsConfig soundsConfig;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, Vector3 warpLocation, TrackConfig track,
                         VehiclesConfig vehicles, CartSpecConfig cartSpec, GatesConfig gates, double gravityConstant, double dragConstant, SoundsConfig soundsConfig) {
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
        this.soundsConfig = soundsConfig;
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

    public Vector3 getWarpLocation() {
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

    public SoundsConfig getSoundsConfig() {
        return soundsConfig;
    }

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");
        Vector3 warpLocation = Vector3.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        double gravityConstant = configurationSection.getDouble("gravityConstant");
        double dragConstant = configurationSection.getDouble("dragConstant");
        TrackConfig track = TrackConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("track")));
        VehiclesConfig vehicles = VehiclesConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("vehicles")));
        CartSpecConfig cartSpec = CartSpecConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("cartSpec")));
        GatesConfig gates = GatesConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("gates")));
        SoundsConfig sounds = SoundsConfig.fromConfigurationSection(Objects.requireNonNull(configurationSection.getConfigurationSection("sounds")));

        return new CoasterConfig(manifestVersion, identifier, displayName, warpLocation, track, vehicles,
                cartSpec, gates, gravityConstant, dragConstant, sounds);
    }
}
