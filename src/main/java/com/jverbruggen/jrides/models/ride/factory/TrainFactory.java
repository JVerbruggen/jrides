package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.CyclicFrame;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.LinkedFrame;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.exception.NoSpawnAvailableException;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
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

    public Train createEquallyDistributedTrain(Track track, String trainIdentifier){
        final int totalFrames = track.getRawPositionsCount();
        final Section spawnSection = track.getNextSpawnSection();
        if(spawnSection == null) throw new NoSpawnAvailableException(track);

        final Frame headOfTrainFrame = CyclicFrame.fromFrame(spawnSection.getEndFrame(), totalFrames);
        final int amountOfCarts = 10;
        final int cartDistance = 41;
        final LinkedFrame massMiddleFrame = new LinkedFrame(headOfTrainFrame, -(amountOfCarts*cartDistance) / 2, totalFrames);
        final int headOfTrainOffset = headOfTrainFrame.getValue();
        final Vector3 cartOffset = new Vector3(0, -1.9, 0);
        final LinkedFrame tailOfTrainFrame = new LinkedFrame(headOfTrainFrame, -(amountOfCarts*cartDistance), totalFrames);

        List<Cart> carts = new ArrayList<>();
        for(int i = 0; i < amountOfCarts; i++){
            int cartOffsetFrames = i*cartDistance;
            int cartFrame = Frame.getCyclicFrameValue(headOfTrainOffset - cartOffsetFrames, totalFrames);
            TrainModelItem model = new TrainModelItem(ItemStackFactory.getCoasterStack(Material.DIAMOND_AXE, 22));

            NoLimitsExportPositionRecord positionRecord = track.getRawPositions().get(cartFrame);
            Vector3 trackLocation = positionRecord.toVector3();
            Quaternion orientation = positionRecord.getOrientation();
            Vector3 cartLocation = Cart.calculateLocation(trackLocation, cartOffset, orientation);

            VirtualArmorstand armorStand = viewportManager.spawnVirtualArmorstand(cartLocation, model);
            Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> armorStand.setHeadpose(ArmorStandPose.getArmorStandPose(orientation)));

            List<Vector3> seatOffsets = List.of(new Vector3(0.3, -1.2, -1.2), new Vector3(0.3, -1.2, -0.4), new Vector3(0.3, -1.2, 0.4), new Vector3(0.3, -1.2, 1.2));
            List<Seat> seats = seatFactory.createSeats(seatOffsets, cartLocation, orientation);

            Cart cart = new SimpleCart(
                    seats,
                    armorStand,
                    cartOffset,
                    new LinkedFrame(headOfTrainFrame, -cartOffsetFrames, totalFrames));
            carts.add(cart);
        }

        Vector3 headLocation = track.getRawPositions().get(headOfTrainFrame.getValue()).toVector3();
        Vector3 middleLocation = track.getRawPositions().get(massMiddleFrame.getValue()).toVector3();
        Vector3 tailLocation = track.getRawPositions().get(tailOfTrainFrame.getValue()).toVector3();
        Train train = new SimpleTrain(trainIdentifier, carts, headOfTrainFrame, massMiddleFrame, tailOfTrainFrame, headLocation, middleLocation, tailLocation, spawnSection);
        spawnSection.addOccupation(train);
        return train;
    }
}
