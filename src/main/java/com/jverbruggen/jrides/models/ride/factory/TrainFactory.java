package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.VehiclesConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.CartModelItemConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.CartTypeSpecConfig;
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
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TrainFactory {
    private final ViewportManager viewportManager;
    private final SeatFactory seatFactory;

    public TrainFactory() {
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.seatFactory = ServiceProvider.getSingleton(SeatFactory.class);
    }

    public Train createEquallyDistributedTrain(Track track, CoasterConfig coasterConfig, String trainIdentifier){
        final int totalFrames = track.getRawPositionsCount();
        final Section spawnSection = track.getNextSpawnSection();
        if(spawnSection == null){
            JRidesPlugin.getLogger().severe("No spawn section available on track " + track.toString() + " for train to spawn!");
            return null;
        }

        VehiclesConfig vehiclesConfig = coasterConfig.getVehicles();

        final Frame headOfTrainFrame = CyclicFrame.fromFrame(spawnSection.getSpawnFrame(), totalFrames);
        final int amountOfCarts = vehiclesConfig.getCarts();
        final int cartDistance = vehiclesConfig.getCartDistance();
        final LinkedFrame massMiddleFrame = new LinkedFrame(headOfTrainFrame, -(amountOfCarts*cartDistance) / 2, totalFrames);
        final int headOfTrainOffset = headOfTrainFrame.getValue();
        final Vector3 cartOffset = coasterConfig.getCartSpec().getDefault().getModel().getPosition();
        final LinkedFrame tailOfTrainFrame = new LinkedFrame(headOfTrainFrame, -(amountOfCarts*cartDistance), totalFrames);

        List<Cart> carts = new ArrayList<>();
        for(int i = 0; i < amountOfCarts; i++){
            int cartOffsetFrames = i*cartDistance;
            int cartFrame = Frame.getCyclicFrameValue(headOfTrainOffset - cartOffsetFrames, totalFrames);

            CartTypeSpecConfig cartTypeSpecConfig = coasterConfig.getCartSpec().getDefault();
            CartModelItemConfig cartModelItemConfig = cartTypeSpecConfig.getModel().getItem();

            Material modelMaterial = cartModelItemConfig.getMaterial();
            int modelDamage = cartModelItemConfig.getDamage();
            boolean unbreakable = cartModelItemConfig.isUnbreakable();
            TrainModelItem model = new TrainModelItem(ItemStackFactory.getCoasterStack(modelMaterial, modelDamage, unbreakable));

            NoLimitsExportPositionRecord positionRecord = track.getRawPositions().get(cartFrame);
            Vector3 trackLocation = positionRecord.toVector3();
            Quaternion orientation = positionRecord.getOrientation();
            Vector3 cartLocation = Cart.calculateLocation(trackLocation, cartOffset, orientation);

            VirtualArmorstand armorStand = viewportManager.spawnVirtualArmorstand(cartLocation, model);
            Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> armorStand.setHeadpose(ArmorStandPose.getArmorStandPose(orientation)));

            List<Vector3> seatOffsets = cartTypeSpecConfig.getSeats().getPositions();
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
