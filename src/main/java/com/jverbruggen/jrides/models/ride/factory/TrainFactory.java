package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.CartSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.VehiclesConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.CartTypeSpecConfig;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.AutoTrackUpdateFrame;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.*;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TrainFactory {
    private final ViewportManager viewportManager;
    private final SeatFactory seatFactory;
    private final boolean debugMode;
    private final SectionProvider sectionProvider;

    public TrainFactory() {
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.seatFactory = ServiceProvider.getSingleton(SeatFactory.class);
        this.sectionProvider = ServiceProvider.getSingleton(SectionProvider.class);
        this.debugMode = false; // TODO: add to a config
    }

    public Train createEquallyDistributedTrain(CoasterHandle coasterHandle, Track track, CoasterConfig coasterConfig, String trainIdentifier){
        final Section spawnSection = track.getNextSpawnSection();
        if(spawnSection == null){
            JRidesPlugin.getLogger().severe("No spawn section available on track " + track + " for train " + trainIdentifier + " to spawn!");
            return null;
        }

        VehiclesConfig vehiclesConfig = coasterConfig.getVehicles();

        final int amountOfCarts = vehiclesConfig.getCarts();
        final int cartDistance = vehiclesConfig.getCartDistance();

        final Frame sectionSpawnFrame = spawnSection.getSpawnFrame();
        final int headOfTrainFrameValue = sectionSpawnFrame.getValue();
        final int middleOfTrainFrameValue = headOfTrainFrameValue - (amountOfCarts*cartDistance) / 2;
        final int tailOfTrainFrameValue = headOfTrainFrameValue - (amountOfCarts*cartDistance);

        final Track spawnTrack = sectionSpawnFrame.getTrack();

        final Frame headOfTrainFrame = new AutoTrackUpdateFrame(headOfTrainFrameValue, spawnTrack, spawnSection);
        final Frame middleOfTrainFrame = new AutoTrackUpdateFrame(middleOfTrainFrameValue, spawnTrack, spawnSection);
        final Frame tailOfTrainFrame = new AutoTrackUpdateFrame(tailOfTrainFrameValue, spawnTrack, spawnSection);

        final Vector3 cartOffset = coasterConfig.getCartSpec().getDefault().getModel().getPosition();

        List<CoasterCart> carts = new ArrayList<>();
        for(int i = 0; i < amountOfCarts; i++){
            int cartOffsetFrames = i*cartDistance;

            Frame cartFrame = headOfTrainFrame.clone().add(-cartOffsetFrames);

            CartSpecConfig cartSpecConfig = coasterConfig.getCartSpec();
            CartTypeSpecConfig cartTypeSpecConfig;
            if(i == 0 && cartSpecConfig.hasHead())
                cartTypeSpecConfig = cartSpecConfig.getHead();
            else if(i == amountOfCarts-1 && cartSpecConfig.hasTail())
                cartTypeSpecConfig = cartSpecConfig.getTail();
            else
                cartTypeSpecConfig = cartSpecConfig.getDefault();

            ItemConfig cartModelItemStackConfig = cartTypeSpecConfig.getModel().getItemConfig();

            Vector3 trackLocation = spawnSection.getLocationFor(cartFrame);
            Quaternion orientation = spawnSection.getOrientationFor(cartFrame);
            Vector3 cartLocation = CoasterCart.calculateLocation(trackLocation, cartOffset, orientation);

            VirtualEntity virtualEntity = cartModelItemStackConfig.spawnEntity(viewportManager, cartLocation);
            Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> virtualEntity.setRotation(orientation));

            List<Vector3> seatOffsets = cartTypeSpecConfig.getSeats().getPositions();
            List<CoasterSeat> seats = seatFactory.createCoasterSeats(coasterHandle, seatOffsets, cartLocation, orientation);

            Section cartSection = spawnSection;
            if(!cartSection.isInSection(cartFrame)){
                cartSection = sectionProvider.findSectionBySearchingPrevious(null, cartFrame, spawnSection);
                if(cartSection == null)
                    throw new RuntimeException("Cant find where cart index=" + i + " should be placed (section-wise)");
            }

            String cartName = trainIdentifier + "_cart_" + i;
            CoasterCart cart = new SimpleCoasterCart(
                    cartName,
                    seats,
                    virtualEntity,
                    cartOffset,
                    cartFrame);
            carts.add(cart);
        }

        Vector3 headLocation = track.getLocationFor(headOfTrainFrame);
        Vector3 middleLocation = track.getLocationFor(middleOfTrainFrame);
        Vector3 tailLocation = track.getLocationFor(tailOfTrainFrame);

        Train train = new SimpleTrain(trainIdentifier, carts, headOfTrainFrame, middleOfTrainFrame, tailOfTrainFrame,
                headLocation, middleLocation, tailLocation, spawnSection, debugMode);
        spawnSection.setLocalReservation(train);
        spawnSection.addOccupation(train);

        return train;
    }

    public void unloadAll(List<TrainHandle> trainHandles) {
        viewportManager.removeEntities(trainHandles);
    }
}
