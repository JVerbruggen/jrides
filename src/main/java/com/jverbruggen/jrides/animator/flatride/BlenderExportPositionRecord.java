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

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;

public class BlenderExportPositionRecord {
    private final String object;
    private final int frame;
    private final float posx;
    private final float posy;
    private final float posz;
    private final float scalex;
    private final float scaley;
    private final float scalez;
    private final float rw;
    private final float rx;
    private final float ry;
    private final float rz;

    public BlenderExportPositionRecord(String object, int frame, float posx, float posy, float posz, float scalex, float scaley, float scalez, float rw, float rx, float ry, float rz) {
        this.object = object;
        this.frame = frame;
        this.posx = posx;
        this.posy = posy;
        this.posz = posz;
        this.scalex = scalex;
        this.scaley = scaley;
        this.scalez = scalez;
        this.rw = rw;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public String getObject() {
        return object;
    }

    public int getFrame() {
        return frame;
    }

    public float getPosx() {
        return posx;
    }

    public float getPosy() {
        return posy;
    }

    public float getPosz() {
        return posz;
    }

    public float getRw() {
        return rw;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    public float getRz() {
        return rz;
    }

    public Vector3 toMinecraftVector(){
        return new Vector3(
                this.getPosx(),
                this.getPosz(), // Minecraft Y = Blender Z
                -this.getPosy());
    }

    public Quaternion toMinecraftQuaternion(){
        return new Quaternion(
                this.getRx(),
                this.getRz(), // Minecraft Y = Blender Z
                -this.getRy(),
                this.getRw());
    }

    public VectorQuaternionState toVectorQuaternionState(){
        return new VectorQuaternionState(
                toMinecraftVector(),
                toMinecraftQuaternion()
        );
    }

    public static BlenderExportPositionRecord createFromCSVAttributes(String[] attributes){
        return new BlenderExportPositionRecord(
                attributes[0],
                Integer.parseInt(attributes[1]),
                Float.parseFloat(attributes[2]),
                Float.parseFloat(attributes[3]),
                Float.parseFloat(attributes[4]),
                Float.parseFloat(attributes[5]),
                Float.parseFloat(attributes[6]),
                Float.parseFloat(attributes[7]),
                Float.parseFloat(attributes[8]),
                Float.parseFloat(attributes[9]),
                Float.parseFloat(attributes[10]),
                Float.parseFloat(attributes[11])
        );
    }
}
