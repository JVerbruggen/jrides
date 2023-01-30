package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.button.SimpleRideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RideControlButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.ButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.controller.ButtonUpdateController;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class LockResembledControlButton extends BaseRideControlButton implements RideControlButton {
    private RideControlButton parentButton;
    private boolean buttonOkState;
    private final ButtonVisual buttonBlockedVisual;
    private final ButtonVisual buttonOkVisual;
    private final DispatchLock lock;

    public LockResembledControlButton(String rideIdentifier, ButtonVisual buttonBlockedVisual, ButtonVisual buttonOkVisual, int slot, DispatchLock lock, RideControlButtonAction action) {
        this.buttonBlockedVisual = buttonBlockedVisual;
        this.buttonOkVisual = buttonOkVisual;
        this.lock = lock;
        this.buttonOkState = isLockOk();

        this.parentButton = new SimpleRideControlButton(
                rideIdentifier, getActiveVisual(), slot, action);

        this.lock.addEventListener(this::onLockEvent);
        ServiceProvider.getSingleton(ButtonUpdateController.class).addButton(this, 10);
    }

    private void onLockEvent(DispatchLock lock){
        updateState();
    }

    private boolean isLockOk(){
        return lock.isUnlocked();
    }

    public void sendUpdate(){
        parentButton.sendUpdate();
    }

    public void changeDisplayName(String displayName){
        parentButton.changeDisplayName(displayName);
    }

    public void changeMaterial(Material material){
        parentButton.changeMaterial(material);
    }

    public void changeTitleColor(ChatColor color){
        parentButton.changeTitleColor(color);
    }

    public void changeLore(List<String> lore){
        parentButton.changeLore(lore);
    }

    public ItemStack getItemStack(){
        return parentButton.getItemStack();
    }

    public void setItemStack(ItemStack itemStack) {
        parentButton.setItemStack(itemStack);
    }

    public void setVisible(boolean visible){
        parentButton.setVisible(visible);
    }

    public void setParentMenu(RideControlMenu parentMenu) {
        parentButton.setParentMenu(parentMenu);
    }

    public RideControlMenu getParentMenu() {
        return parentButton.getParentMenu();
    }

    public UUID getUuid() {
        return parentButton.getUuid();
    }

    public int getSlot() {
        return parentButton.getSlot();
    }

    public void press(Player player){
        parentButton.press(player);
        updateState();
    }

    private void updateState(){
        buttonOkState = isLockOk();
        setButtonVisual(getActiveVisual());
        sendUpdate();
    }

    @Override
    public ButtonVisual getActiveVisual() {
        if(buttonOkState){
            return buttonOkVisual;
        }else return buttonBlockedVisual;
    }

    @Override
    public void updateVisual() {
        setButtonVisual(getActiveVisual());
        sendUpdate();
    }
}
