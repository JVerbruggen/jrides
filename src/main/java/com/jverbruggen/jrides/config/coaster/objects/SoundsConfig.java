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
        String onrideWind = configurationSection.contains("onrideWind") ? configurationSection.getString("onrideWind") : "";
        String restraintOpen = configurationSection.contains("restraintOpen") ? configurationSection.getString("restraintOpen") : "";
        String restraintClose = configurationSection.contains("restraintClose") ? configurationSection.getString("restraintClose") : "";
        String dispatch = configurationSection.contains("dispatch") ? configurationSection.getString("dispatch") : "";

        return new SoundsConfig(onrideWind, restraintOpen, restraintClose, dispatch);
    }
}
