package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.effect.platform.EntityFromToMovementEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

public class EntityFromToMovementConfig extends BaseConfig implements EntityMovementConfig {
    private final String identifier;
    private final ItemConfig itemConfig;

    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final Vector3 rotationFrom;
    private final Vector3 rotationTo;
    private final int animationTimeTicks;

    private final boolean locationHasDelta;
    private final boolean rotationHasDelta;

    public EntityFromToMovementConfig(String identifier, ItemConfig itemConfig, Vector3 locationFrom, Vector3 locationTo, Vector3 rotationFrom, Vector3 rotationTo, int animationTimeTicks) {
        this.identifier = identifier;
        this.itemConfig = itemConfig;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rotationFrom = rotationFrom;
        this.rotationTo = rotationTo;
        this.animationTimeTicks = animationTimeTicks;
        this.locationHasDelta = locationFrom != null && locationTo != null && locationTo.distanceSquared(locationFrom) > 0.01;
        this.rotationHasDelta = rotationFrom != null && rotationTo != null && rotationTo.distanceSquared(rotationFrom) > 0.01;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    public Vector3 getLocationFrom() {
        return locationFrom;
    }

    public Vector3 getLocationTo() {
        return locationTo;
    }

    public Vector3 getRotationFrom() {
        return rotationFrom;
    }

    public Vector3 getRotationTo() {
        return rotationTo;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    public boolean isLocationHasDelta() {
        return locationHasDelta;
    }

    public boolean isRotationHasDelta() {
        return rotationHasDelta;
    }

    public static EntityFromToMovementConfig fromConfigurationSection(String identifier, ConfigurationSection configurationSection){
        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection);

        Vector3 locationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationFrom", null));
        Vector3 locationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationTo", null));
        Vector3 rotationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationFrom", null));
        Vector3 rotationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationTo", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks", 20);

        return new EntityFromToMovementConfig(identifier, itemConfig, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks);
    }

    @Override
    public TrainEffectTrigger createTrigger(ViewportManager viewportManager) {
        String identifier = getIdentifier();

        boolean hasLocationDelta = isLocationHasDelta();
        boolean hasRotationDelta = isRotationHasDelta();

        Vector3 locationFrom = hasLocationDelta ? getLocationFrom() : null;
        Vector3 locationTo = hasLocationDelta ? getLocationTo() : null;

        Quaternion rotationFrom = hasRotationDelta ? Quaternion.fromAnglesVector(getRotationFrom()) : null;
        Quaternion rotationTo = hasRotationDelta ? Quaternion.fromAnglesVector(getRotationTo()) : null;

        ItemConfig itemConfig = getItemConfig();
        VirtualEntity virtualEntity = itemConfig.spawnEntity(viewportManager, locationFrom);
        int animationTimeTicks = getAnimationTimeTicks();

        return new EntityFromToMovementEffectTrigger(identifier, virtualEntity, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks);
    }
}
