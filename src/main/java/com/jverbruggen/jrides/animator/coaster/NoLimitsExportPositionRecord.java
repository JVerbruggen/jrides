package com.jverbruggen.jrides.animator.coaster;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.util.Vector;

public class NoLimitsExportPositionRecord {
    private final int index;
    private final float posX;
    private final float posY;
    private final float posZ;
    private final float frontX;
    private final float frontY;
    private final float frontZ;
    private final float leftX;
    private final float leftY;
    private final float leftZ;
    private final float upX;
    private final float upY;
    private final float upZ;
    private final Quaternion orientation;

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

        Vector dirVector = new Vector(leftX, leftY, leftZ).multiply(-1);
        Vector upVector = new Vector(upX, upY, upZ);
        this.orientation = Quaternion.fromLookDirection(dirVector, upVector);
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

    public Quaternion getOrientation() {
        return orientation;
    }

    public Vector3 toVector3(){
        return new Vector3(posX, posY, posZ);
    }

    public static NoLimitsExportPositionRecord createFromCSVAttributes(String[] attributes, int index,float offsetX, float offsetY, float offsetZ){
        return new NoLimitsExportPositionRecord(
                index,
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
}
