package com.jverbruggen.jrides.config.flatride.structure.rotor.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.rotor.RotorConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RelativeMultipleRotorAttachmentConfig extends BaseConfig implements AttachmentConfig {
    private final String toComponentIdentifier;
    private final Vector3 offsetPosition;
    private final int amount;

    public RelativeMultipleRotorAttachmentConfig(String toComponentIdentifier, Vector3 offsetPosition, int amount) {
        this.toComponentIdentifier = toComponentIdentifier;
        this.offsetPosition = offsetPosition;
        this.amount = amount;
    }

    public String getToComponentIdentifier() {
        return toComponentIdentifier;
    }

    public Vector3 getOffsetPosition() {
        return offsetPosition;
    }

    public int getAmount() {
        return amount;
    }

    public static AttachmentConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String armTo = getString(configurationSection, "arm");
        Vector3 offsetPosition;
        if(configurationSection.contains("armDistance"))
            offsetPosition = new Vector3(getInt(configurationSection, "armDistance"), 0, 0);
        else if(configurationSection.contains("armOffset"))
            offsetPosition = Vector3.fromDoubleList(getDoubleList(configurationSection, "armOffset"));
        else throw new RuntimeException("No arm offset or arm distance specified for rotor");

        int armDuplicate = getInt(configurationSection, "armDuplicate", 1);

        return new RelativeMultipleRotorAttachmentConfig(armTo, offsetPosition, armDuplicate);
    }

    @Override
    public void createRotorWithAttachment(RotorConfig rotorConfig, List<FlatRideComponent> components) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createDistributedAttachedRotors(
                    rotorConfig.getIdentifier(),
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    rotorConfig.getRotorSpeed(),
                    rotorConfig.getFlatRideModels(),
                    getAmount());

            components.addAll(createdComponents);
        }
    }
}
