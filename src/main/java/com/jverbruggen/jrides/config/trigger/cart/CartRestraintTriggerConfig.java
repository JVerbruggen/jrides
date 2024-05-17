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
    public EffectTrigger createTrigger() {
        return new CartRestraintEffectTrigger(locked);
    }
}
