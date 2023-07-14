package com.jverbruggen.jrides.config.flatride.structure.attachment.joint;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public final class RelativeAttachmentJointConfig extends BaseConfig {
    private final boolean isFixedX;
    private final boolean isFixedY;
    private final boolean isFixedZ;
    private final double fixX;
    private final double fixY;
    private final double fixZ;

    public RelativeAttachmentJointConfig(boolean isFixedX, boolean isFixedY, boolean isFixedZ, double fixX, double fixY, double fixZ) {
        this.isFixedX = isFixedX;
        this.isFixedY = isFixedY;
        this.isFixedZ = isFixedZ;
        this.fixX = fixX;
        this.fixY = fixY;
        this.fixZ = fixZ;
    }


    public boolean isFixedX() {
        return isFixedX;
    }

    public boolean isFixedY() {
        return isFixedY;
    }

    public boolean isFixedZ() {
        return isFixedZ;
    }

    public double getFixX() {
        return fixX;
    }

    public double getFixY() {
        return fixY;
    }

    public double getFixZ() {
        return fixZ;
    }

    public boolean anyFixed(){
        return isFixedX || isFixedY || isFixedZ;
    }

    public static RelativeAttachmentJointConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RelativeAttachmentJointConfig(false, false, false, 0, 0, 0);

        Double fixX = null;
        Double fixY = null;
        Double fixZ = null;

        if(configurationSection.contains("fixX"))
            fixX = getDouble(configurationSection, "fixX");
        if(configurationSection.contains("fixY"))
            fixY = getDouble(configurationSection, "fixY");
        if(configurationSection.contains("fixZ"))
            fixZ = getDouble(configurationSection, "fixZ");

        return new RelativeAttachmentJointConfig(fixX != null, fixY != null, fixZ != null,
                getDoubleOrElseNull(fixX), getDoubleOrElseNull(fixY), getDoubleOrElseNull(fixZ));
    }

    private static double getDoubleOrElseNull(Double d){
        if(d == null) return 0d;
        return d;
    }
}
