package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.action.RunnableButtonAction;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class RideDispatchButton implements RideControlButton {
    private RideControlButton parentButton;
    private ButtonState state;
    private boolean buttonOn;
    private final ButtonVisual buttonOffVisual;
    private final ButtonVisual buttonOnVisual;
    private final ButtonVisual noDispatchVisual;

    public RideDispatchButton(RideController rideController, int slot) {
        this.state = ButtonState.NO_DISPATCH;
        this.buttonOffVisual = new ButtonVisual(Material.GREEN_CONCRETE, ChatColor.DARK_GREEN);
        this.buttonOnVisual = new ButtonVisual(Material.LIME_CONCRETE, ChatColor.GREEN);
        this.noDispatchVisual = new ButtonVisual(Material.LIGHT_GRAY_CONCRETE, ChatColor.RED);
        this.parentButton = new SimpleRideControlButton(
                rideController.getRide().getIdentifier(), noDispatchVisual.toItemStack(), slot, getAction(rideController));
        this.buttonOn = true;

        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 10L, 10L);
    }

    public RideControlButtonAction getAction(RideController rideController){
        return new RunnableButtonAction(p -> {
            DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();
            boolean dispatched = dispatchTrigger.execute(p);
            if(dispatched){
                p.sendMessage(ChatColor.GOLD + "Dispatched");
                setDispatching();
            }
        });
    }

    public void tick(){
        if(state != ButtonState.READY_FOR_DISPATCH) return;

        buttonOn = !buttonOn;
        setButtonVisual(buttonOn);
    }

    public void setReadyForDispatch(){
        state = ButtonState.READY_FOR_DISPATCH;
        setButtonVisual(buttonOn);
        parentButton.sendUpdate();
    }

    public void setNoDispatch(){
        state = ButtonState.NO_DISPATCH;
        setButtonVisual(noDispatchVisual);
    }

    public void setDispatching(){
        state = ButtonState.DISPATCHING;
        setButtonVisual(true);
    }

    private void setButtonVisual(boolean on){
        if(on) setButtonVisual(buttonOnVisual);
        else setButtonVisual(buttonOffVisual);
    }

    private void setButtonVisual(ButtonVisual visual){
        parentButton.changeMaterial(visual.getButtonMaterial());
        parentButton.changeTitleColor(visual.getButtonDisplayNameColor());
        parentButton.sendUpdate();
    }

    public void sendUpdate(){
        return;
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
    }
}

enum ButtonState{
    NO_DISPATCH,
    READY_FOR_DISPATCH,
    DISPATCHING
}

class ButtonVisual{
    private final Material buttonMaterial;
    private final ChatColor buttonDisplayNameColor;

    ButtonVisual(Material buttonMaterial, ChatColor buttonDisplayNameColor) {
        this.buttonMaterial = buttonMaterial;
        this.buttonDisplayNameColor = buttonDisplayNameColor;
    }

    ChatColor getButtonDisplayNameColor() {
        return buttonDisplayNameColor;
    }

    Material getButtonMaterial() {
        return buttonMaterial;
    }

    ItemStack toItemStack(){
        return ItemStackFactory.getRideControlButtonStack(getButtonMaterial(), buttonDisplayNameColor + "Dispatch");
    }
}