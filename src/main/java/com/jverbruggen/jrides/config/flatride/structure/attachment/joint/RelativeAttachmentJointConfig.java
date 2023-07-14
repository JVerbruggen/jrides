package com.jverbruggen.jrides.config.flatride.structure.attachment.joint;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;

public final class RelativeAttachmentJointConfig extends BaseConfig {
    private final boolean fixX;
    private final boolean fixY;
    private final boolean fixZ;

    public RelativeAttachmentJointConfig(boolean fixX, boolean fixY, boolean fixZ) {
        this.fixX = fixX;
        this.fixY = fixY;
        this.fixZ = fixZ;
    }

    public static RelativeAttachmentJointConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RelativeAttachmentJointConfig(false, false, false);

        boolean fixX = getBoolean(configurationSection, "fixX", false);
        boolean fixY = getBoolean(configurationSection, "fixY", false);
        boolean fixZ = getBoolean(configurationSection, "fixZ", false);

        return new RelativeAttachmentJointConfig(fixX, fixY, fixZ);
    }
}
