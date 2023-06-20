package com.jverbruggen.jrides.state.ride.menu;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.menu.SimpleMenu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.List;

public class RideOverviewMenuFactory {
    private final LanguageFile languageFile;
    private final RideOverviewMenuButtonFactory rideOverviewMenuButtonFactory;
    private Menu rideOverviewMenu;

    public RideOverviewMenuFactory() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.rideOverviewMenuButtonFactory = ServiceProvider.getSingleton(RideOverviewMenuButtonFactory.class);
        this.rideOverviewMenu = null;
    }

    public Menu createRideOverviewMenu(List<RideHandle> rideHandles){
        int count = rideHandles.size();
        if(count > 9*6)
            // TODO: Large quantity of rides in menu support with pages
            throw new RuntimeException("Menus for a large quantity of rides is not yet supported.");

        SimpleMenu rideOverviewMenu = new SimpleMenu(3, "Ride overview menu");
        for(int i = 0; i < count; i++){
            rideOverviewMenu.addButton(rideOverviewMenuButtonFactory.createRideStatusButton(rideHandles.get(i), i));
        }

        this.rideOverviewMenu = rideOverviewMenu;

        return rideOverviewMenu;
    }

    public Menu getRideOverviewMenu(){
        if(this.rideOverviewMenu == null)
            throw new RuntimeException("Ride overview menu was not created yet, probably due to a previous error");

        return this.rideOverviewMenu;
    }
}
