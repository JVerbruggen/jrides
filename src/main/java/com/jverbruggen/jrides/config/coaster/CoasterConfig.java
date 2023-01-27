package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.CartSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.VehiclesConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class CoasterConfig {
    private String manifestVersion;
    private String identifier;
    private String displayName;
    private Vector3 warpLocation;
    private TrackConfig track;
    private VehiclesConfig vehicles;
    private CartSpecConfig cartSpec;
    private GatesConfig gates;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, Vector3 warpLocation, TrackConfig track,
                         VehiclesConfig vehicles, CartSpecConfig cartSpec, GatesConfig gates) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.warpLocation = warpLocation;
        this.track = track;
        this.vehicles = vehicles;
        this.cartSpec = cartSpec;
        this.gates = gates;
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

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");
        Vector3 warpLocation = Vector3.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        TrackConfig track = TrackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("track"));
        VehiclesConfig vehicles = VehiclesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("vehicles"));
        CartSpecConfig cartSpec = CartSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("cartSpec"));
        GatesConfig gates = GatesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("gates"));

        return new CoasterConfig(manifestVersion, identifier, displayName, warpLocation, track, vehicles, cartSpec, gates);
    }
}
