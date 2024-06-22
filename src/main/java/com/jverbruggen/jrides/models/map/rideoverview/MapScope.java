package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.models.math.MathUtil;

public class MapScope {
    private static final int DRAW_SCREEN_SIZE = 128;
    private static final int DRAW_MARKER_SIZE = 256;

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
            int delta = dZ - dX;
            worldMinX -= delta/2;
            worldMaxX += delta/2;
        }

        this.worldMinX = worldMinX;
        this.worldMinZ = worldMinZ;
        this.worldMaxX = worldMaxX;
        this.worldMaxZ = worldMaxZ;
    }

    public int toScreenX(int mapX){
        return (int)MathUtil.map(mapX, worldMinX, worldMaxX, 0, DRAW_SCREEN_SIZE-1);
    }

    public int toScreenZ(int mapZ){
        return (int)MathUtil.map(mapZ, worldMinZ, worldMaxZ, 0, DRAW_SCREEN_SIZE-1);
    }

    public int toScreenXMarker(int mapX){
        // TODO: Check out of bounds
        return (int)MathUtil.map(mapX, worldMinX, worldMaxX, 0, DRAW_MARKER_SIZE-1);
    }

    public int toScreenZMarker(int mapZ){
        return (int)MathUtil.map(mapZ, worldMinZ, worldMaxZ, 0, DRAW_MARKER_SIZE-1);
    }
}
