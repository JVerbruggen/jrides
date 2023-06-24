package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;

public abstract class AbstractRelativeMultipleAttachmentConfig extends BaseConfig implements AttachmentConfig {
    private final String toComponentIdentifier;
    private final Vector3 offsetPosition;
    private final int amount;

    protected AbstractRelativeMultipleAttachmentConfig(String toComponentIdentifier, Vector3 offsetPosition, int amount) {
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

}
