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

package com.jverbruggen.jrides.config.trigger.cart;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.cart.CartRestraintEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class CartRestraintTriggerConfig extends BaseTriggerConfig {
    public final boolean locked;

    public CartRestraintTriggerConfig(boolean locked) {
        super(TriggerType.CART_RESTRAINT);
        this.locked = locked;
    }

    public static CartRestraintTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        boolean locked = getBoolean(configurationSection, "locked");

        return new CartRestraintTriggerConfig(locked);
    }

    @Override
    public EffectTrigger createTrigger(String rideIdentifier) {
        return new CartRestraintEffectTrigger(locked);
    }
}
