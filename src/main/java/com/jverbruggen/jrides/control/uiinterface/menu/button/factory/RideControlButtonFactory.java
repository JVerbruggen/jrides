package com.jverbruggen.jrides.control.uiinterface.menu.button.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.StationTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.button.LockResembledControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonWithContextAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.BlinkingButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.CabinOccupationVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.SimpleMenuButton;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class RideControlButtonFactory {
    private final LanguageFile languageFile;

    public RideControlButtonFactory(){
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public MenuButton createClaimRideButton(RideController rideController, int slot){
        return new SimpleMenuButton(
                new CabinOccupationVisual(rideController,
                        new StaticButtonVisual(Material.BLACK_CONCRETE_POWDER,
                            ChatColor.GOLD,
                            languageFile.get(LanguageFileFields.BUTTON_CLAIM_CABIN)),
                            languageFile.get(LanguageFileFields.BUTTON_CABIN_CLAIMED)),
                slot, new RunnableButtonWithContextAction((p, b) -> {
            if(p.equals(rideController.getOperator())){
                p.setOperating(null);
            }else{
                p.setOperating(rideController);
            }
        }));
    }

    public MenuButton createRestraintButton(StationTrigger restraintTrigger, int slot){
        return new LockResembledControlButton(
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE,
                                ChatColor.WHITE, languageFile.get(LanguageFileFields.BUTTON_RESTRAINTS_OPEN_STATE)),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE,
                                ChatColor.GRAY, languageFile.get(LanguageFileFields.BUTTON_RESTRAINTS_OPEN_STATE))
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE,
                        ChatColor.WHITE, languageFile.get(LanguageFileFields.BUTTON_RESTRAINTS_CLOSED_STATE)),
                slot, restraintTrigger.getLock(), new RunnableButtonAction(restraintTrigger::execute));
    }

    public MenuButton createGateButton(StationTrigger gateTrigger, int slot){
        return new LockResembledControlButton(
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.WHITE_CONCRETE,
                                ChatColor.WHITE, languageFile.get(LanguageFileFields.BUTTON_GATES_OPEN_STATE)),
                        new StaticButtonVisual(Material.LIGHT_GRAY_CONCRETE,
                                ChatColor.GRAY, languageFile.get(LanguageFileFields.BUTTON_GATES_OPEN_STATE))
                ),
                new StaticButtonVisual(Material.WHITE_CONCRETE,
                        ChatColor.WHITE, languageFile.get(LanguageFileFields.BUTTON_GATES_CLOSED_STATE)),
                slot, gateTrigger.getLock(), new RunnableButtonAction(gateTrigger::execute));
    }

    public MenuButton createProblemList(DispatchLockCollection dispatchLockCollection, int slot){
        MenuButton problemList = new SimpleMenuButton(
                new StaticButtonVisual(Material.ITEM_FRAME,
                        ChatColor.RED, languageFile.get(LanguageFileFields.BUTTON_PROBLEMS_STATE)),
                slot, null);
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

    public MenuButton createDispatchButton(DispatchTrigger dispatchTrigger, int slot){
        return new LockResembledControlButton(
                new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN,
                        languageFile.get(LanguageFileFields.BUTTON_DISPATCH_STATE),
                        List.of(ChatColor.GRAY + languageFile.get(LanguageFileFields.BUTTON_DISPATCH_PROBLEM_STATE))),
                new BlinkingButtonVisual(
                        new StaticButtonVisual(Material.LIME_CONCRETE,
                                ChatColor.GREEN, languageFile.get(LanguageFileFields.BUTTON_DISPATCH_STATE)),
                        new StaticButtonVisual(Material.GREEN_CONCRETE,
                                ChatColor.DARK_GREEN, languageFile.get(LanguageFileFields.BUTTON_DISPATCH_STATE))
                ),
                slot, dispatchTrigger.getDispatchLockCollection(), new RunnableButtonAction(dispatchTrigger::execute));
    }

    public MenuButton createStateOpenRideButton(RideHandle rideHandle, int slot){
        return new SimpleMenuButton(
                new StaticButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN, "Open ride"),
                slot,
                new RunnableButtonAction(rideHandle::open)
        );
    }

    public MenuButton createStateCloseRideButton(RideHandle rideHandle, int slot){
        return new SimpleMenuButton(
                new StaticButtonVisual(Material.RED_CONCRETE, ChatColor.RED, "Close ride"),
                slot,
                new RunnableButtonAction(rideHandle::close)
        );
    }
}
