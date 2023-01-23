package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.CartSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.VehiclesConfig;
import org.bukkit.configuration.ConfigurationSection;

public class CoasterConfig {
    private String manifestVersion;
    private String identifier;
    private String displayName;
    private TrackConfig track;
    private VehiclesConfig vehicles;
    private CartSpecConfig cartSpec;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, TrackConfig track,
                         VehiclesConfig vehicles, CartSpecConfig cartSpec) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.track = track;
        this.vehicles = vehicles;
        this.cartSpec = cartSpec;
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

    public TrackConfig getTrack() {
        return track;
    }

    public VehiclesConfig getVehicles() {
        return vehicles;
    }

    public CartSpecConfig getCartSpec() {
        return cartSpec;
    }

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");
        TrackConfig track = TrackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("track"));
        VehiclesConfig vehicles = VehiclesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("vehicles"));
        CartSpecConfig cartSpec = CartSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("cartSpec"));

        return new CoasterConfig(manifestVersion, identifier, displayName, track, vehicles, cartSpec);
    }
}
