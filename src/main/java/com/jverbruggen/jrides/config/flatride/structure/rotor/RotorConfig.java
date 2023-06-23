package com.jverbruggen.jrides.config.flatride.structure.rotor;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeed;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.FixedAttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleRotorAttachmentConfigConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RotorConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final boolean root;
    private final String rotorAxis;
    private final String rotorControl;
    private final String rotorControlPlayerAcc;
    private final RotorSpeed rotorSpeed;
    private final AttachmentConfig<RotorConfig> attachmentConfig;
    private final List<ModelConfig> flatRideModels;

    public RotorConfig(String identifier, boolean root, String rotorAxis, String rotorControl, String rotorControlPlayerAcc, RotorSpeed rotorSpeed, AttachmentConfig<RotorConfig> attachmentConfig, List<ModelConfig> flatRideModels) {
        this.identifier = identifier;
        this.root = root;
        this.rotorAxis = rotorAxis;
        this.rotorControl = rotorControl;
        this.rotorControlPlayerAcc = rotorControlPlayerAcc;
        this.rotorSpeed = rotorSpeed;
        this.attachmentConfig = attachmentConfig;
        this.flatRideModels = flatRideModels;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, RideHandle rideHandle) {
        attachmentConfig.createWithAttachment(this, components, rideHandle);
    }

    public boolean isRoot() {
        return root;
    }

    public String getRotorAxis() {
        return rotorAxis;
    }

    public String getRotorControl() {
        return rotorControl;
    }

    public String getRotorControlPlayerAcc() {
        return rotorControlPlayerAcc;
    }

    public RotorSpeed getRotorSpeed() {
        return rotorSpeed;
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        boolean root = getBoolean(configurationSection, "root", false);
        String axis = getString(configurationSection, "axis", "y");
        String rotorControl = getString(configurationSection, "rotorControl", "auto");
        String rotorControlPlayerAcc = getString(configurationSection, "rotorControlPlayerAcc", "none");

        AttachmentConfig<RotorConfig> attachmentConfig = null;
        if(configurationSection.contains("position")){
            attachmentConfig = FixedAttachmentConfig.fromConfigurationSection(configurationSection);
        }
        if(configurationSection.contains("arm")){
            if(attachmentConfig != null) throw new RuntimeException("Can only create one attachment type for a rotor");
            attachmentConfig = RelativeMultipleRotorAttachmentConfigConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for rotor");

        RotorSpeed rotorSpeed;
        if(configurationSection.contains("maxSpeed") || configurationSection.contains("minSpeed")){
            rotorSpeed = new RotorSpeed(
                    (float)getDouble(configurationSection, "speed"),
                    (float)getDouble(configurationSection, "minSpeed"),
                    (float)getDouble(configurationSection, "maxSpeed"));
        }else{
            rotorSpeed = new RotorSpeed((float)getDouble(configurationSection, "speed", 1));
        }

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        return new RotorConfig(identifier, root, axis, rotorControl, rotorControlPlayerAcc, rotorSpeed, attachmentConfig, modelConfigs);
    }
}
