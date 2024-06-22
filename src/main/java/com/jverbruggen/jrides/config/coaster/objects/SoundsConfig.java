package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

public class SoundsConfig {
    private final String onrideWind;
    private final String restraintOpen;
    private final String restraintClose;
    private final String dispatch;

    public SoundsConfig(String onrideWind, String restraintOpen, String restraintClose, String dispatch) {
        this.onrideWind = onrideWind;
        this.restraintOpen = restraintOpen;
        this.restraintClose = restraintClose;
        this.dispatch = dispatch;
    }

    public SoundsConfig() {
        this(null, null, null, null);
    }

    public String getOnrideWind() {
        return onrideWind;
    }

    public String getRestraintOpen() {
        return restraintOpen;
    }

    public String getRestraintClose() {
        return restraintClose;
    }

    public String getDispatch() {
        return dispatch;
    }

    public static SoundsConfig fromConfigurationSection(ConfigurationSection configurationSection){
        if(configurationSection == null) return new SoundsConfig();

        String onrideWind = configurationSection.contains("onrideWind") ? configurationSection.getString("onrideWind") : null;
        String restraintOpen = configurationSection.contains("restraintOpen") ? configurationSection.getString("restraintOpen") : null;
        String restraintClose = configurationSection.contains("restraintClose") ? configurationSection.getString("restraintClose") : null;
        String dispatch = configurationSection.contains("dispatch") ? configurationSection.getString("dispatch") : null;

        return new SoundsConfig(onrideWind, restraintOpen, restraintClose, dispatch);
    }
}
