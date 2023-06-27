package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.FixedAttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LinearActuatorConfig extends AbstractActuatorConfig{
    private final String axis;
    private final float size;
    private final Supplier<Integer> phase;

    public LinearActuatorConfig(String identifier, boolean root, String axis, float size, Supplier<Integer> phase, AttachmentConfig attachmentConfig, FlatRideComponentSpeed flatRideComponentSpeed, List<ModelConfig> flatRideModelsConfig) {
        super(identifier, root, attachmentConfig, flatRideComponentSpeed, flatRideModelsConfig);
        this.axis = axis;
        this.size = size;
        this.phase = phase;
    }

    public String getAxis() {
        return axis;
    }

    public float getSize() {
        return size;
    }

    public Supplier<Integer> getPhase() {
        return phase;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, RideHandle rideHandle) {
        getAttachmentConfig().createLinearActuator(this, components, rideHandle);
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        boolean root = getBoolean(configurationSection, "root", false);
        String axis = getString(configurationSection, "axis", "y");
        float size = (float)getDouble(configurationSection, "size", 1d);
        Supplier<Integer> phase = getIntSupplier(configurationSection, "phase", 0);
        String control = getString(configurationSection, "control", "continuous");

        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("position")){
            attachmentConfig = FixedAttachmentConfig.fromConfigurationSection(configurationSection);
        }
        if(configurationSection.contains("arm")){
            if(attachmentConfig != null) throw new RuntimeException("Can only create one attachment type for a linear actuator");
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for linear actuator");

        FlatRideComponentSpeed flatRideComponentSpeed;
        if(configurationSection.contains("minSpeed")) {
            flatRideComponentSpeed = new FlatRideComponentSpeed(
                    (float) getDouble(configurationSection, "speed"),
                    (float) getDouble(configurationSection, "minSpeed"),
                    (float) getDouble(configurationSection, "maxSpeed"));
        }else{
            flatRideComponentSpeed = new FlatRideComponentSpeed(
                    (float)getDouble(configurationSection, "speed", 0));
        }

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        return new LinearActuatorConfig(identifier, root, axis, size, phase, attachmentConfig, flatRideComponentSpeed, modelConfigs);
    }
}
