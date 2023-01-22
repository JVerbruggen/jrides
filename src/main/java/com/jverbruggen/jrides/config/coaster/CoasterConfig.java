package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import org.bukkit.configuration.ConfigurationSection;

public class CoasterConfig {
    private String manifestVersion;
    private String identifier;
    private String displayName;
    private TrackConfig track;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, TrackConfig track) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.track = track;
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

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");
        TrackConfig track = TrackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("track"));

        return new CoasterConfig(manifestVersion, identifier, displayName, track);
    }
}
