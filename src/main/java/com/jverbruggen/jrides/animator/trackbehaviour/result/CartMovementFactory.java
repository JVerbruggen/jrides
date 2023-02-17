package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.HashMap;
import java.util.List;

public class CartMovementFactory {
    private final SectionProvider sectionProvider;

    public CartMovementFactory() {
        this.sectionProvider = ServiceProvider.getSingleton(SectionProvider.class);
    }

    public HashMap<Cart, CartMovement> createOnTrackCartMovement(TrainHandle trainHandle, List<Cart> carts, int speedFrameIncrement, Section section){
        HashMap<Cart, CartMovement> cartMovements = new HashMap<>();

        for(Cart cart : carts){
            Frame cartFrame = cart.getFrame();
            Frame newShadedCartFrame = cartFrame.clone().add(speedFrameIncrement);

            sectionProvider.addFramesWithSectionLogic(trainHandle, cartFrame, newShadedCartFrame.getValue());

            Section cartSection = cartFrame.getSection();
            Vector3 cartPositionOnTrack = cartSection.getLocationFor(cartFrame);
            Quaternion orientation = cartSection.getOrientationFor(cartFrame).clone();
            if(cartFrame.isInvertedFrameAddition())
                orientation.rotateY(180);

            Vector3 cartPosition = Cart.calculateLocation(cartPositionOnTrack, cart.getTrackOffset(), orientation);

            cartMovements.put(cart, new CartMovement(cartPosition, orientation));
        }

        return cartMovements;
    }
}
