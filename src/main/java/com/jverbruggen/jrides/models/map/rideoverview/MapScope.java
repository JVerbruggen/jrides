package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.models.math.MathUtil;

public class MapScope {
    private int worldMinX;
    private int worldMinZ;
    private int worldMaxX;
    private int worldMaxZ;

    public void setNew(int worldMinX, int worldMinZ, int worldMaxX, int worldMaxZ){
        // Preserving aspect ratio 1:1
        int dX = worldMaxX - worldMinX;
        int dZ = worldMaxZ - worldMinZ;
        if(dX > dZ){
            int delta = dX - dZ;
            worldMinZ -= delta/2;
            worldMaxZ += delta/2;
        }else if(dZ > dX){
            int delta = dX - dZ;
            worldMinX -= delta/2;
            worldMaxX += delta/2;
        }

        this.worldMinX = worldMinX;
        this.worldMinZ = worldMinZ;
        this.worldMaxX = worldMaxX;
        this.worldMaxZ = worldMaxZ;
    }

    public int toScreenX(int mapX){
        return (int)MathUtil.map(mapX, worldMinX, worldMaxX, 0, 127);
    }

    public int toScreenZ(int mapZ){
        return (int)MathUtil.map(mapZ, worldMinZ, worldMaxZ, 0, 127);
    }

    public int toScreenXMarker(int mapX){
        return (int)MathUtil.map(mapX, worldMinX, worldMaxX, 0, 255);
    }

    public int toScreenZMarker(int mapZ){
        return (int)MathUtil.map(mapZ, worldMinZ, worldMaxZ, 0, 255);
    }
}
