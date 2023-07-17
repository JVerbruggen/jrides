package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.FixedAttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RotorConfig extends AbstractActuatorConfig implements StructureConfigItem {
    private final String rotorAxis;
    private final RotorPlayerControlConfig playerControlConfig;


    public RotorConfig(String identifier, boolean root, String rotorAxis, RotorPlayerControlConfig playerControlConfig, FlatRideComponentSpeed flatRideComponentSpeed, AttachmentConfig attachmentConfig, List<ModelConfig> flatRideModels) {
        super(identifier, root, attachmentConfig, flatRideComponentSpeed, flatRideModels);
        this.rotorAxis = rotorAxis;
        this.playerControlConfig = playerControlConfig;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        getAttachmentConfig().createRotorWithAttachment(this, components, rideHandle);
    }

    public String getRotorAxis() {
        return rotorAxis;
    }

    public RotorPlayerControlConfig getPlayerControlConfig() {
        return playerControlConfig;
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        boolean root = getBoolean(configurationSection, "root", false);
        String axis = getString(configurationSection, "axis", "y");
        RotorPlayerControlConfig playerControlConfig = RotorPlayerControlConfig.fromConfigurationSection(identifier, configurationSection.getConfigurationSection("control"));

        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("position")){
            attachmentConfig = FixedAttachmentConfig.fromConfigurationSection(configurationSection);
        }
        if(configurationSection.contains("arm")){
            if(attachmentConfig != null) throw new RuntimeException("Can only create one attachment type for a rotor");
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for rotor");

        FlatRideComponentSpeed flatRideComponentSpeed;
        if(configurationSection.contains("maxSpeed") || configurationSection.contains("minSpeed")){
            flatRideComponentSpeed = new FlatRideComponentSpeed(
                    (float)getDouble(configurationSection, "speed"),
                    (float)getDouble(configurationSection, "minSpeed"),
                    (float)getDouble(configurationSection, "maxSpeed"));
        }else{
            flatRideComponentSpeed = new FlatRideComponentSpeed((float)getDouble(configurationSection, "speed", 1));
        }

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        return new RotorConfig(identifier, root, axis, playerControlConfig, flatRideComponentSpeed, attachmentConfig, modelConfigs);
    }
}
