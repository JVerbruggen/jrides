package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.effect.platform.EntityContinuousMovementEffectTrigger;
import com.jverbruggen.jrides.effect.platform.EntityMovementTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class EntityContinuousMovementConfig extends BaseConfig implements EntityMovementConfig {
    private final Vector3 initialLocation;
    private final Vector3 initialRotation;
    private final boolean resetOnStart;

    private final Vector3 locationDelta;
    private final Vector3 rotationDelta;
    private final int animationTimeTicks;

    public EntityContinuousMovementConfig(Vector3 initialLocation, Vector3 initialRotation, boolean resetOnStart, Vector3 locationDelta, Vector3 rotationDelta, int animationTimeTicks) {
        this.initialLocation = initialLocation;
        this.initialRotation = initialRotation;
        this.resetOnStart = resetOnStart;
        this.locationDelta = locationDelta;
        this.rotationDelta = rotationDelta;
        this.animationTimeTicks = animationTimeTicks;
    }

    public static EntityContinuousMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 initialLocation = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialLocation", null));
        Vector3 initialRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialRotation", null));
        Vector3 locationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationDelta", null));
        Vector3 rotationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationDelta", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks", 20);
        boolean resetOnStart = getBoolean(configurationSection, "resetOnStart", true);

        return new EntityContinuousMovementConfig(initialLocation, initialRotation, resetOnStart, locationDelta, rotationDelta, animationTimeTicks);
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        Quaternion initialRotation = Quaternion.fromAnglesVector(this.initialRotation);
        virtualEntity.setRotation(initialRotation);

        Quaternion rotationDelta = Quaternion.fromAnglesVector(this.rotationDelta);

        return new EntityContinuousMovementEffectTrigger(virtualEntity, initialLocation, initialRotation, resetOnStart, locationDelta, rotationDelta, animationTimeTicks);
    }

    @Override
    public Vector3 getInitialLocation() {
        return initialLocation;
    }
}
