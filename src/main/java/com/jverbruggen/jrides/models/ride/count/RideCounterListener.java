package com.jverbruggen.jrides.models.ride.count;

import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.event.player.PlayerQuitEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideCounterListener implements Listener {
    private final RideCounterManager rideCounterManager;

    public RideCounterListener() {
        rideCounterManager = ServiceProvider.getSingleton(RideCounterManager.class);
    }

    @EventHandler
    public void onFinishRide(PlayerFinishedRideEvent event){
        Player player = event.getPlayer();
        String rideIdentifier = event.getRideIdentifier();

        RideCounterRecordCollection collection = rideCounterManager.getCollection(player);
        RideCounterRecord record = collection.findOrCreate(rideIdentifier);

        record.addOne();

        rideCounterManager.sendRideCounterUpdateMessage(player, record);
        rideCounterManager.saveToFile(player.getIdentifier(), collection);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        rideCounterManager.saveAndUnload(player.getIdentifier());
    }
}