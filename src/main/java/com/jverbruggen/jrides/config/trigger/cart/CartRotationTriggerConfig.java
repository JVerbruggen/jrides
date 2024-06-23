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
import com.jverbruggen.jrides.effect.cart.rotation.CartRotationEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class CartRotationTriggerConfig extends BaseTriggerConfig {
    private final Vector3 rotation;
    private final int animationTicks;

    public CartRotationTriggerConfig(Vector3 rotation, int animationTicks) {
        super(TriggerType.CART_ROTATE);
        this.rotation = rotation;
        this.animationTicks = animationTicks;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public int getAnimationTicks() {
        return animationTicks;
    }

    public static CartRotationTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 rotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotation"));
        int animationTicks = getInt(configurationSection, "animationTicks", 20);

        return new CartRotationTriggerConfig(rotation, animationTicks);
    }

    @Override
    public EffectTrigger createTrigger() {
        return new CartRotationEffectTrigger(getRotation(), getAnimationTicks());
    }
}
