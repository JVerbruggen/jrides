package com.jverbruggen.jrides.control.uiinterface.menu;

import com.jverbruggen.jrides.models.entity.Player;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class SimpleRideControlButton implements RideControlButton {
    private final String rideIdentifier;
    private ItemStack itemStack;
    private int slot;
    private UUID uuid;
    private RideControlButtonAction action;
    private RideControlMenu parentMenu;
    private boolean visible;
    private boolean hasUpdate;

    public SimpleRideControlButton(String rideIdentifier, ItemStack itemStack, int slot, RideControlButtonAction action) {
        this.rideIdentifier = rideIdentifier;
        this.slot = slot;
        this.action = action;
        this.visible = true;
        this.hasUpdate = false;

        this.uuid = UUID.randomUUID();
        setItemStack(itemStack);
    }

    @Override
    public void sendUpdate(){
        if(!hasUpdate) return;

        hasUpdate = false;

        getParentMenu().getSessions().forEach((player, inventory) -> {
            inventory.setItem(slot, getItemStack());
        });
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
        nbtItem.setString(BUTTON_RIDE_IDENTIFIER_KEY, rideIdentifier);

        this.itemStack = nbtItem.getItem();

        hasUpdate = true;
    }

    @Override
    public void setVisible(boolean visible){
        this.visible = visible;

        hasUpdate = true;
    }

    @Override
    public void setParentMenu(RideControlMenu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Override
    public RideControlMenu getParentMenu() {
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
        action.run(player);
    }

    public static String BUTTON_UUID_KEY = "jrides-button-uuid";
    public static String BUTTON_RIDE_IDENTIFIER_KEY = "jrides-button-ride-identifier";
}
