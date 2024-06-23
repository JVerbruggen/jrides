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

package com.jverbruggen.jrides.models.entity.armorstand;

import com.jverbruggen.jrides.models.math.Vector3;

public class ArmorstandRotations {
    private Vector3 head, body, offHand, mainHand, leftLeg, rightLeg;

    public ArmorstandRotations(){
        head = new Vector3(0,0,0);
        body = new Vector3(0,0,0);
        offHand = new Vector3(0,0,0);
        mainHand = new Vector3(0,0,0);
        leftLeg = new Vector3(0,0,0);
        rightLeg = new Vector3(0,0,0);
    }

    public Vector3 getHead() {
        return head;
    }

    public void setHead(Vector3 head) {
        this.head = head;
    }

    public Vector3 getBody() {
        return body;
    }

    public void setBody(Vector3 body) {
        this.body = body;
    }

    public Vector3 getOffHand() {
        return offHand;
    }

    public void setOffHand(Vector3 offHand) {
        this.offHand = offHand;
    }

    public Vector3 getMainHand() {
        return mainHand;
    }

    public void setMainHand(Vector3 mainHand) {
        this.mainHand = mainHand;
    }

    public Vector3 getLeftLeg() {
        return leftLeg;
    }

    public void setLeftLeg(Vector3 leftLeg) {
        this.leftLeg = leftLeg;
    }

    public Vector3 getRightLeg() {
        return rightLeg;
    }

    public void setRightLeg(Vector3 rightLeg) {
        this.rightLeg = rightLeg;
    }
}
