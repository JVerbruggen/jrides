package com.jverbruggen.jrides.effect.cart.rotation;

import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.config.trigger.cart.CartRotationTriggerConfig;
import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import com.jverbruggen.jrides.effect.train.music.ExternalMusicEffectTrigger;

public class CartRotationTriggerFactory {
    public CartRotationEffectTrigger getRotationEffectTrigger(TriggerConfig triggerConfig){
        CartRotationTriggerConfig cartRotationTriggerConfig = (CartRotationTriggerConfig) triggerConfig;

        return new CartRotationEffectTrigger(cartRotationTriggerConfig.getRotation(), cartRotationTriggerConfig.getAnimationTicks());
    }
}
