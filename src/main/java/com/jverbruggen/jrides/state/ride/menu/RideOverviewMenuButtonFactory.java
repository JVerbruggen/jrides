package com.jverbruggen.jrides.state.ride.menu;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StatefulButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.SimpleMenuButton;
import com.jverbruggen.jrides.models.ride.state.OpenState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RideOverviewMenuButtonFactory {
    private final LanguageFile languageFile;

    public RideOverviewMenuButtonFactory() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public MenuButton createRideStatusButton(RideHandle rideHandle, int slot){
        HashMap<OpenState, ButtonVisual> options = new HashMap<>();
        ItemStack displayItem = rideHandle.getRide().getDisplayItem();
        if(displayItem == null) displayItem = new ItemStack(Material.STONE, 1);

        List<String> lore = rideHandle.getRide().getDisplayDescription();
        List<String> openLore = new ArrayList<>(lore);
        List<String> closedLore = new ArrayList<>(lore);
        List<String> maintenanceLore = new ArrayList<>(lore);

        openLore.add(ChatColor.GREEN + "This ride is currently opened");
        closedLore.add(ChatColor.RED + "This ride is currently closed");
        maintenanceLore.add(ChatColor.DARK_GRAY + "This ride is currently in maintenance");

        options.put(OpenState.OPEN, new StaticButtonVisual(displayItem, ChatColor.GOLD, rideHandle.getRide().getDisplayName(), openLore));
        options.put(OpenState.CLOSED, new StaticButtonVisual(displayItem, ChatColor.RED, rideHandle.getRide().getDisplayName(), closedLore));
        options.put(OpenState.MAINTENANCE, new StaticButtonVisual(displayItem, ChatColor.GRAY, rideHandle.getRide().getDisplayName(), maintenanceLore));

        return new SimpleMenuButton(
                new StatefulButtonVisual<>(() -> rideHandle.getState().getOpenState(), options, OpenState.MAINTENANCE),
                slot,
                new RunnableButtonAction(p -> p.teleport(rideHandle.getRide().getWarpLocation()))
        );
    }
}