package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.HashMap;
import java.util.List;

public class CartMovementFactory {

    public HashMap<Cart, CartMovement> createOnTrackCartMovement(List<Cart> carts, Section section){
        HashMap<Cart, CartMovement> cartMovements = new HashMap<>();

        for(Cart cart : carts){
            Frame cartFrame = cart.getFrame();
            Vector3 cartPositionOnTrack = section.getLocationFor(cartFrame);
            Quaternion orientation = section.getOrientationFor(cartFrame);

            Vector3 cartPosition = Cart.calculateLocation(cartPositionOnTrack, cart.getTrackOffset(), orientation);

            cartMovements.put(cart, new CartMovement(cartPosition, orientation));
        }

        return cartMovements;
    }
}
