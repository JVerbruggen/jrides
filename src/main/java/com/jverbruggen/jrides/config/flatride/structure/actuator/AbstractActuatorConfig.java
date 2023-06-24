package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;

import java.util.List;

public abstract class AbstractActuatorConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final boolean root;
    private final AttachmentConfig attachmentConfig;
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final List<ModelConfig> flatRideModels;

    public AbstractActuatorConfig(String identifier, boolean root, AttachmentConfig attachmentConfig, FlatRideComponentSpeed flatRideComponentSpeed, List<ModelConfig> flatRideModels) {
        this.identifier = identifier;
        this.root = root;
        this.attachmentConfig = attachmentConfig;
        this.flatRideComponentSpeed = flatRideComponentSpeed;
        this.flatRideModels = flatRideModels;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public boolean isRoot() {
        return root;
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }
}
