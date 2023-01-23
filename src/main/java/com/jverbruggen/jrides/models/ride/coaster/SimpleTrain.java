package com.jverbruggen.jrides.models.ride.coaster;

import java.util.List;

public class SimpleTrain implements Train {
    private List<Cart> carts;
    private int cartDistance;
    private int headOfTrainOffset;

    public SimpleTrain(List<Cart> carts, int cartDistance, int headOfTrainOffset) {
        this.carts = carts;
        this.cartDistance = cartDistance;
        this.headOfTrainOffset = headOfTrainOffset;
    }

    @Override
    public List<Cart> getCarts() {
        return carts;
    }

    @Override
    public int getCartDistanceFor(int index) {
        return cartDistance*index;
    }
}
