package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.effect.platform.ArmorstandMovementEffectTrigger;
import com.jverbruggen.jrides.exception.CoasterLoadException;
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
    private final List<ArmorstandMovementConfig> armorstands;

    public MultiArmorstandMovementConfig(List<ArmorstandMovementConfig> armorstands) {
        super(TriggerType.MULTI_ARMORSTAND_MOVEMENT);
        this.armorstands = armorstands;
    }

    public List<ArmorstandMovementConfig> getArmorstands() {
        return armorstands;
    }

    public List<ArmorstandMovementEffectTrigger> create(ViewportManager viewportManager){
        List<ArmorstandMovementEffectTrigger> armorstandMovements = new ArrayList<>();
        for(ArmorstandMovementConfig armorstandMovementConfig : getArmorstands()) {
            ItemStack itemStack = ItemStackFactory.getCoasterStack(
                    armorstandMovementConfig.getMaterial(),
                    armorstandMovementConfig.getDamage(),
                    armorstandMovementConfig.isUnbreakable());

            String identifier = armorstandMovementConfig.getIdentifier();

            boolean hasLocationDelta = armorstandMovementConfig.isLocationHasDelta();
            boolean hasRotationDelta = armorstandMovementConfig.isRotationHasDelta();

            Vector3 locationFrom = hasLocationDelta ? armorstandMovementConfig.getLocationFrom() : null;
            Vector3 locationTo = hasLocationDelta ? armorstandMovementConfig.getLocationTo() : null;

            Quaternion rotationFrom = hasRotationDelta ? Quaternion.fromAnglesVector(armorstandMovementConfig.getRotationFrom()) : null;
            Quaternion rotationTo = hasRotationDelta ? Quaternion.fromAnglesVector(armorstandMovementConfig.getRotationTo()) : null;

            TrainModelItem model = new TrainModelItem(itemStack);
            VirtualArmorstand armorstand = viewportManager.spawnVirtualArmorstand(locationFrom, 0, model);
            int animationTimeTicks = armorstandMovementConfig.getAnimationTimeTicks();

            armorstandMovements.add(new ArmorstandMovementEffectTrigger(identifier, armorstand, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks));
        }

        return armorstandMovements;
    }

    public static MultiArmorstandMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection armorstandsSection = configurationSection.getConfigurationSection("armorstands");
        if(armorstandsSection == null) throw new RuntimeException("Trigger section needs 'armorstands' key");

        Set<String> armorstandsIdentifiers = armorstandsSection.getKeys(false);

        List<ArmorstandMovementConfig> armorstands = new ArrayList<>();
        for(String identifier : armorstandsIdentifiers){
            ArmorstandMovementConfig movementConfig = ArmorstandMovementConfig.fromConfigurationSection(identifier, armorstandsSection.getConfigurationSection(identifier));
            armorstands.add(movementConfig);
        }

        return new MultiArmorstandMovementConfig(armorstands);
    }
}
