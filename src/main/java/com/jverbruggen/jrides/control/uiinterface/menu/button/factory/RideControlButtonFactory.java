package com.jverbruggen.jrides.control.uiinterface.menu.button.factory;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.button.LockResembledControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.SimpleRideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonWithContextAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.BlinkingButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.CabinOccupationVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class RideControlButtonFactory {
    private final LanguageFile languageFile;

    public RideControlButtonFactory(){
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public RideControlButton createClaimRideButton(RideController rideController, String rideIdentifier){
        return new SimpleRideControlButton(
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
    }

    public RideControlButton createRestraintButton(String rideIdentifier, StationTrigger restraintTrigger){
        return new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonRestraintsOpenState),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, languageFile.buttonRestraintsOpenState)
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonRestraintsClosedState),
                16, restraintTrigger.getLock(), new RunnableButtonAction(restraintTrigger::execute));
    }

    public RideControlButton createGateButton(String rideIdentifier, StationTrigger gateTrigger){
        return new LockResembledControlButton(
                rideIdentifier,
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonGatesOpenState),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.GRAY, languageFile.buttonGatesOpenState)
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE, ChatColor.WHITE, languageFile.buttonGatesClosedState),
                15, gateTrigger.getLock(), new RunnableButtonAction(gateTrigger::execute));
    }

    public RideControlButton createProblemList(String rideIdentifier, DispatchLockCollection dispatchLockCollection){
        RideControlButton problemList = new SimpleRideControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.ITEM_FRAME, ChatColor.RED, languageFile.buttonProblemsState),
                11, null);
        problemList.changeLore(dispatchLockCollection.getProblems(1));

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

        return problemList;
    }

    public RideControlButton createDispatchButton(String rideIdentifier, DispatchTrigger dispatchTrigger){
        return new LockResembledControlButton(
                rideIdentifier,
                new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN,
                        languageFile.buttonDispatchState, List.of(ChatColor.GRAY + languageFile.buttonDispatchProblemState)),
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.LIME_CONCRETE, ChatColor.GREEN, languageFile.buttonDispatchState),
                        new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN, languageFile.buttonDispatchState)
                ),
                10, dispatchTrigger.getDispatchLockCollection(), new RunnableButtonAction(dispatchTrigger::execute));
    }
}
