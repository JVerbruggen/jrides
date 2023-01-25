package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.HashMap;
import java.util.List;

public class CartMovementFactory {

    public HashMap<Cart, CartMovement> createOnTrackCartMovement(List<Cart> carts, Track track){
        HashMap<Cart, CartMovement> cartMovements = new HashMap<>();

        for(Cart cart : carts){
            int cartFrame = cart.getFrame().getValue();
            NoLimitsExportPositionRecord cartPositionOnTrackRecord = track.getRawPositions().get(cartFrame);
            Vector3 cartPositionOnTrack = cartPositionOnTrackRecord.toVector3();
            Quaternion orientation = cartPositionOnTrackRecord.getOrientation();

            Vector3 cartPosition = Cart.calculateLocation(cartPositionOnTrack, cart.getTrackOffset(), orientation);

            cartMovements.put(cart, new CartMovement(cartPosition, orientation));
        }

        return cartMovements;
    }
}