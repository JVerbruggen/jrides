package com.jverbruggen.jrides.config.coaster.objects.section.point;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SwitchSectionSpecConfig extends BaseConfig {
    private final List<String> destinations;
    private final String handler;
    private final String origin;

    public SwitchSectionSpecConfig(List<String> destinations, String handler, String origin) {
        this.destinations = destinations;
        this.handler = handler;
        this.origin = origin;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public String getHandler() {
        return handler;
    }

    public String getOrigin() {
        return origin;
    }

    public static SwitchSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<String> destinations = getStringList(configurationSection, "destinations");
        String handler = getString(configurationSection, "handler", "roundrobin");
        String origin = getString(configurationSection, "origin");

        return new SwitchSectionSpecConfig(destinations, handler, origin);
    }
}
