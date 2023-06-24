package com.jverbruggen.jrides.config.flatride.structure.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SeatConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final AttachmentConfig attachmentConfig;
    private final List<ModelConfig> flatRideModels;
    private final int seatYawOffset;

    public SeatConfig(String identifier, AttachmentConfig attachmentConfig, List<ModelConfig> flatRideModels, int seatYawOffset) {
        this.identifier = identifier;
        this.attachmentConfig = attachmentConfig;
        this.flatRideModels = flatRideModels;
        this.seatYawOffset = seatYawOffset;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, RideHandle rideHandle) {
        attachmentConfig.createSeatWithAttachment(this, components, rideHandle);
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }


    public int getSeatYawOffset() {
        return seatYawOffset;
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("arm")){
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for rotor");

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        int seatYawOffset = getInt(configurationSection, "seatRotationDegrees");

        return new SeatConfig(identifier, attachmentConfig, modelConfigs, seatYawOffset);
    }

}
