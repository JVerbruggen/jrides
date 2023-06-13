package com.jverbruggen.jrides.models.menu;

import com.jverbruggen.jrides.models.entity.Player;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class SimpleMenuButton implements MenuButton {
    private final ButtonVisual buttonVisual;
    private ItemStack itemStack;
    private int slot;
    private UUID uuid;
    private MenuButtonAction action;
    private Menu parentMenu;
    private boolean visible;
    private boolean hasUpdate;

    public SimpleMenuButton(ButtonVisual visual, int slot, MenuButtonAction action) {
        this.slot = slot;
        this.action = action;
        this.visible = true;
        this.hasUpdate = false;
        this.buttonVisual = visual;

        this.uuid = UUID.randomUUID();
        setItemStack(visual.toItemStack());
    }

    @Override
    public void sendUpdate(){
        if(buttonVisual.hasUpdate()){
            setButtonVisual(buttonVisual);
        }

        if(hasUpdate){
            hasUpdate = false;

            getParentMenu().getSessions().forEach((player, inventory) -> inventory.setItem(slot, getItemStack()));
        }
    }

    @Override
    public void changeDisplayName(String displayName){
        ItemStack newItemStack = itemStack.clone();
        ItemMeta itemMeta = newItemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        newItemStack.setItemMeta(itemMeta);

        itemStack = newItemStack;

        hasUpdate = true;
    }

    @Override
    public void changeMaterial(Material material){
        if(itemStack.getType() == material) return;

        ItemStack newItemStack = itemStack.clone();
        newItemStack.setType(material);

        itemStack = newItemStack;

        hasUpdate = true;
    }

    @Override
    public void changeTitleColor(ChatColor color){
        String displayName = itemStack.getItemMeta().getDisplayName();
        displayName = ChatColor.stripColor(displayName);
        changeDisplayName(color + displayName);
    }

    @Override
    public void changeLore(List<String> lore){
        ItemStack newItemStack = itemStack.clone();
        ItemMeta itemMeta = newItemStack.getItemMeta();

        itemMeta.setLore(lore);
        newItemStack.setItemMeta(itemMeta);

        itemStack = newItemStack;

        hasUpdate = true;
    }

    @Override
    public ItemStack getItemStack(){
        if(!visible) return null;

        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString(BUTTON_UUID_KEY, uuid.toString());

        this.itemStack = nbtItem.getItem();

        hasUpdate = true;
    }

    @Override
    public void setVisible(boolean visible){
        this.visible = visible;

        hasUpdate = true;
    }

    @Override
    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Override
    public Menu getParentMenu() {
        return parentMenu;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void press(Player player){
        if(action == null) return;
        action.run(player, this);
        player.playSound(getPressedSound());
        sendUpdate();
    }

    @Override
    public ButtonVisual getActiveVisual() {
        return buttonVisual;
    }

    @Override
    public void updateVisual() {
        setItemStack(buttonVisual.toItemStack());
    }

    @Override
    public Sound getPressedSound() {
        return Sound.UI_BUTTON_CLICK;
    }

    private void setButtonVisual(ButtonVisual visual){
        visual.clearUpdate();

        if(visual.needsFullItemStackReload()){
            setItemStack(visual.toItemStack());
            return;
        }

        changeMaterial(visual.getButtonMaterial());
        changeDisplayName(visual.getButtonDisplayNameColor() + visual.getValue());
        changeLore(visual.getLore());
    }

    public static String BUTTON_UUID_KEY = "jrides-button-uuid";
}
