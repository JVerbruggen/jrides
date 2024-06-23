/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
