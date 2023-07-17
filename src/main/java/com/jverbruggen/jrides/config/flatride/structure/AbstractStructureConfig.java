package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;

import java.util.List;

public abstract class AbstractStructureConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final boolean root;
    private final List<ModelConfig> flatRideModels;
    private final AttachmentConfig attachmentConfig;

    public AbstractStructureConfig(String identifier, boolean root, List<ModelConfig> flatRideModels, AttachmentConfig attachmentConfig) {
        this.identifier = identifier;
        this.root = root;
        this.flatRideModels = flatRideModels;
        this.attachmentConfig = attachmentConfig;
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

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }
}
