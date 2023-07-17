package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.effect.platform.ArmorstandMovementEffectTrigger;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiArmorstandMovementConfig extends BaseTriggerConfig{
    private final List<ArmorstandAtoBMovementConfig> armorstands;

    public MultiArmorstandMovementConfig(List<ArmorstandAtoBMovementConfig> armorstands) {
        super(TriggerType.MULTI_ARMORSTAND_MOVEMENT);
        this.armorstands = armorstands;
    }

    public List<ArmorstandAtoBMovementConfig> getArmorstands() {
        return armorstands;
    }

    public List<ArmorstandMovementEffectTrigger> create(ViewportManager viewportManager){
        List<ArmorstandMovementEffectTrigger> armorstandMovements = new ArrayList<>();
        for(ArmorstandAtoBMovementConfig armorstandAtoBMovementConfig : getArmorstands()) {
            ItemStack itemStack = ItemStackFactory.getCoasterStack(
                    armorstandAtoBMovementConfig.getMaterial(),
                    armorstandAtoBMovementConfig.getDamage(),
                    armorstandAtoBMovementConfig.isUnbreakable());

            String identifier = armorstandAtoBMovementConfig.getIdentifier();

            boolean hasLocationDelta = armorstandAtoBMovementConfig.isLocationHasDelta();
            boolean hasRotationDelta = armorstandAtoBMovementConfig.isRotationHasDelta();

            Vector3 locationFrom = hasLocationDelta ? armorstandAtoBMovementConfig.getLocationFrom() : null;
            Vector3 locationTo = hasLocationDelta ? armorstandAtoBMovementConfig.getLocationTo() : null;

            Quaternion rotationFrom = hasRotationDelta ? Quaternion.fromAnglesVector(armorstandAtoBMovementConfig.getRotationFrom()) : null;
            Quaternion rotationTo = hasRotationDelta ? Quaternion.fromAnglesVector(armorstandAtoBMovementConfig.getRotationTo()) : null;

            TrainModelItem model = new TrainModelItem(itemStack);
            VirtualArmorstand armorstand = viewportManager.spawnVirtualArmorstand(locationFrom, 0, model);
            int animationTimeTicks = armorstandAtoBMovementConfig.getAnimationTimeTicks();

            armorstandMovements.add(new ArmorstandMovementEffectTrigger(identifier, armorstand, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks));
        }

        return armorstandMovements;
    }

    public static MultiArmorstandMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection armorstandsSection = configurationSection.getConfigurationSection("armorstands");
        if(armorstandsSection == null) throw new RuntimeException("Trigger section needs 'armorstands' key");

        Set<String> armorstandsIdentifiers = armorstandsSection.getKeys(false);

        List<ArmorstandAtoBMovementConfig> armorstands = new ArrayList<>();
        for(String identifier : armorstandsIdentifiers){
            ArmorstandAtoBMovementConfig movementConfig = ArmorstandAtoBMovementConfig.fromConfigurationSection(identifier, armorstandsSection.getConfigurationSection(identifier));
            armorstands.add(movementConfig);
        }

        return new MultiArmorstandMovementConfig(armorstands);
    }
}
