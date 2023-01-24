package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.exception.NoSpawnAvailableException;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TrainFactory {
    private final ViewportManager viewportManager;
    private final SeatFactory seatFactory;

    public TrainFactory(ViewportManager viewportManager, SeatFactory seatFactory) {
        this.viewportManager = viewportManager;
        this.seatFactory = seatFactory;
    }

    public Train createEquallyDistributedTrain(Track track){
        Section spawnSection = track.getNextSpawnSection();
        if(spawnSection == null) throw new NoSpawnAvailableException(track);

        int indexOffset = spawnSection.getEndFrame();
        int amountOfCarts = 10;
        int distancePerCart = 40;
        int firstCartFromMassMiddleOffset = (amountOfCarts*distancePerCart) / 2;
        int firstCartAbsoluteOffset = indexOffset + firstCartFromMassMiddleOffset;
        Vector3 cartOffset = new Vector3(0, -1.9, 0);

        List<Cart> carts = new ArrayList<>();
        for(int i = 0; i < amountOfCarts; i++){
            int cartOffsetFrames = firstCartAbsoluteOffset - (i*distancePerCart);
            int cartMassMiddleOffset = firstCartFromMassMiddleOffset - (i*distancePerCart);
            TrainModelItem model = new TrainModelItem(ItemStackFactory.getCoasterStack(Material.DIAMOND_AXE, 22));

            NoLimitsExportPositionRecord positionRecord = track.getRawPositions().get(cartOffsetFrames);
            Vector3 trackLocation = positionRecord.toVector3();
            Quaternion orientation = positionRecord.getOrientation();
            Vector3 cartLocation = Cart.calculateLocation(trackLocation, cartOffset, orientation);

            VirtualArmorstand armorStand = viewportManager.spawnVirtualArmorstand(cartLocation, model);

            List<Vector3> seatOffsets = List.of(new Vector3(0.3, -1.2, -1.2), new Vector3(0.3, -1.2, -0.4), new Vector3(0.3, -1.2, 0.4), new Vector3(0.3, -1.2, 1.2));
            List<Seat> seats = seatFactory.createSeats(seatOffsets, cartLocation, orientation);

            Cart cart = new SimpleCart(
                    seats,
                    armorStand,
                    cartOffset,
                    cartMassMiddleOffset);
            carts.add(cart);
        }

        Train train = new SimpleTrain(carts, distancePerCart, firstCartFromMassMiddleOffset, indexOffset, spawnSection);
        spawnSection.setOccupation(train);
        return train;
    }
}
