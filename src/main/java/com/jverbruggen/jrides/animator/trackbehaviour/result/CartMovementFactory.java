package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;

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

//            Bukkit.broadcastMessage("CART FRAME -- INC " + speedFrameIncrement);
            sectionProvider.addFramesWithSectionLogic(trainHandle, cartFrame, newShadedCartFrame.getValue());
//            Bukkit.broadcastMessage("END CART FRAME -- ");

            Section cartSection = cartFrame.getSection();
            Vector3 cartPositionOnTrack = cartSection.getLocationFor(cartFrame);
            Bukkit.broadcastMessage(cartFrame + "");
            Bukkit.broadcastMessage(cartPositionOnTrack.getY() + " y");
            Quaternion orientation = cartSection.getOrientationFor(cartFrame);

            Vector3 cartPosition = Cart.calculateLocation(cartPositionOnTrack, cart.getTrackOffset(), orientation);

            cartMovements.put(cart, new CartMovement(cartPosition, orientation));
        }

        return cartMovements;
    }
}
