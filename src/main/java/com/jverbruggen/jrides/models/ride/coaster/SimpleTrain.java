package com.jverbruggen.jrides.models.ride.coaster;

import java.util.List;

public class SimpleTrain implements Train {
    private List<Cart> carts;

    public SimpleTrain(List<Cart> carts) {
        this.carts = carts;
    }

    @Override
    public List<Cart> getCarts() {
        return carts;
    }
}
