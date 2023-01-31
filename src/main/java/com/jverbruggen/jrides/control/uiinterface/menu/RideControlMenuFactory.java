package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.button.LockResembledControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.SimpleRideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonWithContextAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.BlinkingButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.CabinOccupationVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RideControlMenuFactory {
    private final Map<Player, RideControlMenu> openRideControlMenus;
    private final LanguageFile languageFile;

    public RideControlMenuFactory() {
        this.openRideControlMenus = new HashMap<>();
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public RideControlMenu getControlMenu(RideController rideController){
        DispatchLockCollection dispatchLockCollection = rideController.getTriggerContext().getDispatchLockCollection();
        String rideIdentifier = rideController.getRide().getIdentifier();

        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
        StationTrigger gateTrigger = rideController.getTriggerContext().getGateTrigger();
        StationTrigger restraintTrigger = rideController.getTriggerContext().getRestraintTrigger();

        RideControlButton claimOperatingButton = new SimpleRideControlButton(
                rideIdentifier,
                new CabinOccupationVisual(rideController, new StaticButtonVisual(Material.BLACK_CONCRETE_POWDER, ChatColor.GOLD, languageFile.buttonClaimCabin),
                        languageFile.buttonCabinClaimed),
                4, new RunnableButtonWithContextAction((p, b) -> {
                    if(p.equals(rideController.getOperator())){
                        p.setOperating(null);
                        languageFile.sendMessage(p, languageFile.notificationRideControlInactive,
                                builder -> builder.add(LanguageFileTags.rideIdentifier, rideIdentifier));
                    }else{
                        boolean set = p.setOperating(rideController);
                        if(set)
                            languageFile.sendMessage(p, languageFile.notificationRideControlActive,
                                    builder -> builder.add(LanguageFileTags.rideIdentifier, rideIdentifier));
                    }
        }));

        RideControlButton dispatchButton = new LockResembledControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN,
                        languageFile.buttonDispatchState, List.of(ChatColor.GRAY + languageFile.buttonDispatchProblemState)),
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.LIME_CONCRETE, ChatColor.GREEN, languageFile.buttonDispatchState),
                        new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN, languageFile.buttonDispatchState)
                ),
                10, dispatchTrigger.getDispatchLockCollection(), new RunnableButtonAction(dispatchTrigger::execute));

        RideControlButton problemList = new SimpleRideControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.ITEM_FRAME, ChatColor.RED, languageFile.buttonProblemsState),
                11, null);
        problemList.changeLore(dispatchLockCollection.getProblems(1));

        RideControlButton gateButton = new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonGatesOpenState),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, languageFile.buttonGatesOpenState)
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonGatesClosedState),
                15, gateTrigger.getLock(), new RunnableButtonAction(gateTrigger::execute));
        RideControlButton restraintButton = new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonRestraintsOpenState),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, languageFile.buttonRestraintsOpenState)
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonRestraintsClosedState),
                16, restraintTrigger.getLock(), new RunnableButtonAction(restraintTrigger::execute));

        dispatchLockCollection.addEventListener(lock -> {
            List<String> problems = dispatchLockCollection.getProblems(1);
            if(problems.size() == 0){
                problemList.setVisible(false);
            }else{
                problemList.setVisible(true);
                problemList.changeLore(problems);
            }

            problemList.sendUpdate();
        });

        RideControlMenu rideControlMenu = new RideControlMenu(rideController);
        rideControlMenu.addButton(claimOperatingButton);
        rideControlMenu.addButton(dispatchButton);
        rideControlMenu.addButton(problemList);
        rideControlMenu.addButton(gateButton);
        rideControlMenu.addButton(restraintButton);

        return rideControlMenu;
    }

    public void addOpenRideControlMenu(Player player, RideControlMenu rideControlMenu, Inventory inventory){
        rideControlMenu.addSession(player, inventory);
        openRideControlMenus.put(player, rideControlMenu);
    }

    public RideControlMenu getOpenRideControlMenu(Player player){
        return openRideControlMenus.get(player);
    }

    public void removeOpenRideControlMenu(Player player){
        RideControlMenu rideControlMenu = openRideControlMenus.get(player);
        if(rideControlMenu == null) return;

        rideControlMenu.removeSession(player);
        openRideControlMenus.remove(player);
    }

    public boolean hasOpenRideControlMenu(Player player){
        return openRideControlMenus.containsKey(player);
    }

    private ItemStack getItemStack(Material material, String displayName){
        return ItemStackFactory.getRideControlButtonStack(material, displayName);
    }

    private ItemStack getItemStack(Material material, String displayName, List<String> lore){
        return ItemStackFactory.getRideControlButtonStack(material, displayName, lore);
    }
}
