package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.config.trigger.ArmorstandMovementConfig;
import com.jverbruggen.jrides.config.trigger.MultiArmorstandMovementConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MultiArmorstandMovementEffectTriggerFactory {
    private final ViewportManager viewportManager;

    public MultiArmorstandMovementEffectTriggerFactory() {
        viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public MultiArmorstandMovementEffectTrigger getMultiArmorstandMovementEffectTrigger(TriggerConfig triggerConfig){
        if(!(triggerConfig instanceof MultiArmorstandMovementConfig))
            throw new RuntimeException("Incorrect config for effect trigger, expected multi-armorstand-movement");

        MultiArmorstandMovementConfig multiArmorstandMovementConfig = (MultiArmorstandMovementConfig) triggerConfig;

        List<ArmorstandMovementEffectTrigger> armorstandMovements = new ArrayList<>();
        for(ArmorstandMovementConfig armorstandMovementConfig : multiArmorstandMovementConfig.getArmorstands()){
            ItemStack itemStack = ItemStackFactory.getCoasterStack(
                    armorstandMovementConfig.getMaterial(),
                    armorstandMovementConfig.getDamage(),
                    armorstandMovementConfig.isUnbreakable());

            String identifier = armorstandMovementConfig.getIdentifier();
            Vector3 locationFrom = armorstandMovementConfig.getLocationFrom();
            Vector3 locationTo = armorstandMovementConfig.getLocationTo();
            TrainModelItem model = new TrainModelItem(itemStack);
            VirtualArmorstand armorstand = viewportManager.spawnVirtualArmorstand(locationFrom, 0, model);
            int animationTimeTicks = armorstandMovementConfig.getAnimationTimeTicks();

            armorstandMovements.add(new ArmorstandMovementEffectTrigger(identifier, armorstand, locationFrom, locationTo, animationTimeTicks));
        }

        return new MultiArmorstandMovementEffectTrigger(armorstandMovements);
    }
}
