package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.packets.Packet;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityEquipmentPacket implements Packet {
    private final ProtocolManager protocolManager;
    private final int entityId;
    private final EnumWrappers.ItemSlot itemSlot;
    private final TrainModelItem model;

    public EntityEquipmentPacket(ProtocolManager protocolManager, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.itemSlot = itemSlot;
        this.model = model;
    }

    @Override
    public boolean send(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.entityId);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> data = new ArrayList<>();
        data.add(new Pair<>(itemSlot, model.getItemStack()));
        packet.getSlotStackPairLists().write(0, data);
        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
        }catch(InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }
}
