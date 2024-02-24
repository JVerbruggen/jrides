package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.AbstractStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.FixedAttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class LimbConfig extends AbstractStructureConfig {

    public LimbConfig(String identifier, boolean root, AttachmentConfig attachmentConfig, List<ModelConfig> flatRideModelsConfig) {
        super(identifier, root, flatRideModelsConfig, attachmentConfig);
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        getAttachmentConfig().createLimb(this, components, rideHandle);
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        boolean root = getBoolean(configurationSection, "root", false);

        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("position")){
            attachmentConfig = FixedAttachmentConfig.fromConfigurationSection(configurationSection);
        }
        if(configurationSection.contains("arm")){
            if(attachmentConfig != null) throw new RuntimeException("Can only create one attachment type for a linear actuator");
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for linear actuator");

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        return new LimbConfig(identifier, root, attachmentConfig, modelConfigs);
    }
}
