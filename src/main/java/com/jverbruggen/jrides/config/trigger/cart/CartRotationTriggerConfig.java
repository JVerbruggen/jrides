package com.jverbruggen.jrides.config.trigger.cart;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.cart.rotation.CartRotationEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
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
