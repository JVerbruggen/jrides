package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.models.math.Vector3;

public class NoLimitsExportPositionRecord {
    private int index;
    private float posX;
    private float posY;
    private float posZ;
    private float frontX;
    private float frontY;
    private float frontZ;
    private float leftX;
    private float leftY;
    private float leftZ;
    private float upX;
    private float upY;
    private float upZ;

    public NoLimitsExportPositionRecord(int index, float posX, float posY, float posZ, float frontX, float frontY, float frontZ, float leftX, float leftY, float leftZ, float upX, float upY, float upZ) {
        this.index = index;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.frontX = frontX;
        this.frontY = frontY;
        this.frontZ = frontZ;
        this.leftX = leftX;
        this.leftY = leftY;
        this.leftZ = leftZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    public int getIndex() {
        return index;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public float getFrontX() {
        return frontX;
    }

    public float getFrontY() {
        return frontY;
    }

    public float getFrontZ() {
        return frontZ;
    }

    public float getLeftX() {
        return leftX;
    }

    public float getLeftY() {
        return leftY;
    }

    public float getLeftZ() {
        return leftZ;
    }

    public float getUpX() {
        return upX;
    }

    public float getUpY() {
        return upY;
    }

    public float getUpZ() {
        return upZ;
    }

    public static NoLimitsExportPositionRecord createFromCSVAttributes(String[] attributes, float offsetX, float offsetY, float offsetZ){
        return new NoLimitsExportPositionRecord(
                Integer.parseInt(attributes[0]),
                Float.parseFloat(attributes[1]) + offsetX,
                Float.parseFloat(attributes[2]) + offsetY,
                Float.parseFloat(attributes[3]) + offsetZ,
                Float.parseFloat(attributes[4]),
                Float.parseFloat(attributes[5]),
                Float.parseFloat(attributes[6]),
                Float.parseFloat(attributes[7]),
                Float.parseFloat(attributes[8]),
                Float.parseFloat(attributes[9]),
                Float.parseFloat(attributes[10]),
                Float.parseFloat(attributes[11]),
                Float.parseFloat(attributes[12])
        );
    }

    public static NoLimitsExportPositionRecord createFromCSVAttributes(String[] attributes){
        return createFromCSVAttributes(attributes, 0, 0, 0);
    }

    public Vector3 toVector3(){
        return new Vector3(posX, posY, posZ);
    }
}
