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

package com.jverbruggen.jrides.animator.flatride.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class RelativeAttachment implements Attachment {
    private FlatRideComponent parent;
    private FlatRideComponent child;
    private Quaternion offsetRotation;
    private Vector3 offsetPosition;
    private Matrix4x4 cacheMatrix;

    public RelativeAttachment(FlatRideComponent parent, FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition) {
        this.parent = parent;
        this.child = child;
        this.offsetRotation = offsetRotation;
        this.offsetPosition = offsetPosition;
        this.cacheMatrix = null;

        update();
    }

    @Override
    public Vector3 getPosition() {
        return cacheMatrix.toVector3();
    }

    @Override
    public Quaternion getRotation() {
        return cacheMatrix.getRotation();
    }

    @Override
    public Matrix4x4 getCachedMatrix() {
        return cacheMatrix.clone();
    }

    @Override
    public FlatRideComponent getChild() {
        return child;
    }

    @Override
    public FlatRideComponent getParent() {
        return parent;
    }

    @Override
    public void update() {
        cacheMatrix = MatrixMath.rotateTranslate(
                parent.getPosition(),
                parent.getRotation(),
                this.offsetPosition,
                this.offsetRotation);
    }
}
