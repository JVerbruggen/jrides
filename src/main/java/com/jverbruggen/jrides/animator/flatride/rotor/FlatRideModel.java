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

package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class FlatRideModel {
    private final VirtualEntity entity;
    private final Vector3 offset;
    private final Quaternion rotationOffset;

    public FlatRideModel(VirtualEntity entity, Vector3 offset, Quaternion rotationOffset) {
        this.entity = entity;
        this.offset = offset;
        this.rotationOffset = rotationOffset;
    }

    public void updateLocation(Vector3 parentLocation, Quaternion parentRotation){
        Matrix4x4 matrix = MatrixMath.rotateTranslate(parentLocation, parentRotation, offset, rotationOffset);
        entity.setLocation(matrix.toVector3());
        entity.setRotation(matrix.getRotation());
    }

    public VirtualEntity getEntity() {
        return entity;
    }
}
