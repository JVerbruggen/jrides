package com.jverbruggen.jrides.animator.coaster.trackbehaviour.result;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
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

    public HashMap<CoasterCart, CartMovement> createOnTrackCartMovement(TrainHandle trainHandle, List<CoasterCart> carts, int speedFrameIncrement, Section section){
        HashMap<CoasterCart, CartMovement> cartMovements = new HashMap<>();

        for(CoasterCart cart : carts){
            Frame cartFrame = cart.getFrame();
            Frame newShadedCartFrame = cartFrame.clone().add(speedFrameIncrement);

            sectionProvider.addFramesWithSectionLogic(trainHandle, cartFrame, newShadedCartFrame.getValue());

            Section cartSection = cartFrame.getSection();
            Vector3 cartPositionOnTrack = cartSection.getLocationFor(cartFrame);
            Quaternion orientation = cartSection.getOrientationFor(cartFrame).clone();
            if(cartFrame.isInvertedFrameAddition())
                orientation.rotateY(180);

            VectorQuaternionState vectorQuaternionState = CoasterCart.calculateLocation(cartPositionOnTrack, cart.getTrackOffset(), orientation, cart.getRotationOffset());

            cartMovements.put(cart, new CartMovement(vectorQuaternionState.getVector(), vectorQuaternionState.getQuaternion()));
        }

        return cartMovements;
    }
}
