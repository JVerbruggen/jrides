package com.jverbruggen.jrides.config.coaster;

import com.jverbruggen.jrides.config.coaster.objects.*;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.config.ride.AbstractRideConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoasterConfig extends AbstractRideConfig {
    private final TrackConfig track;
    private final VehiclesConfig vehicles;
    private final CartSpecConfig cartSpec;
    private final double gravityConstant;
    private final double dragConstant;
    private final ControllerConfig controllerConfig;
    private final int rideOverviewMapId;

    public CoasterConfig(String manifestVersion, String identifier, String displayName, List<String> displayDescription,
                         ItemStackConfig displayItem, PlayerLocation warpLocation, TrackConfig track,
                         VehiclesConfig vehicles, CartSpecConfig cartSpec, GatesConfig gates, double gravityConstant, double dragConstant,
                         ControllerConfig controllerConfig, SoundsConfig soundsConfig, int rideOverviewMapId, boolean canExitDuringRide) {
        super(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, gates, soundsConfig, canExitDuringRide);
        this.track = track;
        this.vehicles = vehicles;
        this.cartSpec = cartSpec;
        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
        this.controllerConfig = controllerConfig;
        this.rideOverviewMapId = rideOverviewMapId;
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

    public double getGravityConstant() {
        return gravityConstant;
    }

    public double getDragConstant() {
        return dragConstant;
    }

    public ControllerConfig getControllerConfig() {
        return controllerConfig;
    }

    public int getRideOverviewMapId() {
        return rideOverviewMapId;
    }

    public static CoasterConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = getString(configurationSection, "manifestVersion");
        String identifier = getString(configurationSection, "identifier");
        String displayName = getString(configurationSection, "displayName");

        List<String> displayDescription = Arrays.stream(getString(configurationSection, "displayDescription", "").split("\\\\n"))
                .map(d -> ChatColor.GRAY + d)
                .collect(Collectors.toList());
        if(displayDescription.size() == 1 && ChatColor.stripColor(displayDescription.get(0)).equals(""))
            displayDescription.clear();

        ItemStackConfig displayItem = ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("displayItem"));
        PlayerLocation warpLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        double gravityConstant = getDouble(configurationSection, "gravityConstant", 0.15);
        double dragConstant = getDouble(configurationSection, "dragConstant", 0.9993);
        TrackConfig track = TrackConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "track"));
        VehiclesConfig vehicles = VehiclesConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "vehicles"));
        CartSpecConfig cartSpec = CartSpecConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "cartSpec"));
        GatesConfig gates = GatesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("gates"));
        SoundsConfig sounds = SoundsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("sounds"));
        ControllerConfig controllerConfig = ControllerConfig.fromConfigurationSection(configurationSection.getConfigurationSection("controller"));
        int rideOverviewMapId = getInt(configurationSection, "rideOverviewMapId", -1);
        boolean canExitDuringRide = getBoolean(configurationSection, "canExitDuringRide", false);

        return new CoasterConfig(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, track, vehicles,
                cartSpec, gates, gravityConstant, dragConstant, controllerConfig, sounds, rideOverviewMapId,
                canExitDuringRide);
    }
}
