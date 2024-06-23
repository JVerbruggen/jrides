/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.attachment.RelativeAttachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInterconnectedFlatRideComponent extends AbstractFlatRideComponent {
    private final List<Attachment> children;
    private final RelativeAttachmentJointConfig joint;

    public AbstractInterconnectedFlatRideComponent(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.joint = joint;
        this.children = new ArrayList<>();
    }

    @Override
    public void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition){
        Attachment attachment = new RelativeAttachment(this, child, offsetRotation, offsetPosition);

        child.setAttachedTo(attachment);
        children.add(attachment);
    }

    @Override
    public void tick() {
        for(Attachment attachment : getChildren()){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }

    @Override
    public Quaternion getRotation() {
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) throw new RuntimeException("Interconnected component " + getIdentifier() + " not attached to anything");
        Quaternion rotation = attachedTo.getRotation();

        if(joint != null && joint.anyFixed()){
            double pitch = joint.isFixedX() ? -rotation.getPitch() + joint.getFixX() : 0;
            double yaw = joint.isFixedY() ? -rotation.getYaw() + joint.getFixY() : 0;
            double roll = joint.isFixedZ() ? -rotation.getRoll() + joint.getFixZ() : 0;
            rotation.rotateYawPitchRoll(pitch, yaw, roll);
        }

        return rotation;
    }


    public List<Attachment> getChildren() {
        return children;
    }
}
