package com.jverbruggen.jrides.animator;

public class NoLimitsExportPositionRecord {
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

    public NoLimitsExportPositionRecord(float posX, float posY, float posZ, float frontX, float frontY, float frontZ, float leftX, float leftY, float leftZ, float upX, float upY, float upZ) {
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

    public static NoLimitsExportPositionRecord createFromCSVAttributes(String[] attributes){
        return new NoLimitsExportPositionRecord(
                // index=0 is 'No.', so not necessary
                Float.parseFloat(attributes[1]),
                Float.parseFloat(attributes[2]),
                Float.parseFloat(attributes[3]),
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
}
