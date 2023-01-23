package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.*;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TrainFactory {
    private final ViewportManager viewportManager;

    public TrainFactory(ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    public Train createEquallyDistributedTrain(Track track){
        Section spawnSection = track.getNextSpawnSection();
        int indexOffset = spawnSection.getEndFrame();
        int amountOfCarts = 10;
        int distancePerCart = 40;
        int firstCartFromMassMiddleOffset = (amountOfCarts*distancePerCart) / 2;
        int firstCartAbsoluteOffset = indexOffset + firstCartFromMassMiddleOffset;
        Vector3 trackOffset = new Vector3(0, -1.2, 0);

        List<Cart> carts = new ArrayList<>();
        for(int i = 0; i < amountOfCarts; i++){
            int cartOffset = firstCartAbsoluteOffset - (i*distancePerCart);
            int cartMassMiddleOffset = firstCartFromMassMiddleOffset - (i*distancePerCart);
            TrainModelItem model = new TrainModelItem(ItemStackFactory.getCoasterStack(Material.DIAMOND_AXE, 22));
            VirtualArmorstand armorStand = viewportManager.spawnVirtualArmorstand(
                    track.getRawPositions().get(cartOffset).toVector3(), model);
            Cart cart = new SimpleCart(
                    armorStand,
                    trackOffset,
                    cartMassMiddleOffset);
            carts.add(cart);
        }

        return new SimpleTrain(carts, distancePerCart, firstCartFromMassMiddleOffset);
    }
}
