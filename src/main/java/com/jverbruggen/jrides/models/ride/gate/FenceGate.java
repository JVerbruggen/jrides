package com.jverbruggen.jrides.models.ride.gate;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.DispatchLock;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;

public class FenceGate implements Gate {
    private final String name;
    private final DispatchLock dispatchLock;
    private final Block block;
    private boolean isOpen;

    public FenceGate(String name, DispatchLock dispatchLock, Block block) {
        this.name = name;
        this.dispatchLock = dispatchLock;
        this.block = block;
        this.isOpen = false;
    }

    public String getName() {
        return name;
    }

    @Override
    public void open() {
        if(isOpen) return;

        isOpen = true;
        dispatchLock.lock();
        setBukkitGateState(true);
    }

    @Override
    public void close() {
        if(!isOpen) return;

        isOpen = false;
        dispatchLock.unlock();
        setBukkitGateState(false);
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    private void setBukkitGateState(boolean open){
        BlockData blockData = block.getBlockData();
        if(!(blockData instanceof org.bukkit.block.data.type.Gate)){
            JRidesPlugin.getLogger().severe("Fence gate " + name + " not positioned over actual fence gate block (location: " + block.getLocation() + ")");
            return;
        }

        Openable bukkitGate = (Openable) blockData;
        bukkitGate.setOpen(open);
        block.setBlockData(bukkitGate);

        Sound sound = open ? Sound.BLOCK_FENCE_GATE_OPEN : Sound.BLOCK_FENCE_GATE_CLOSE;
        block.getWorld().playSound(block.getLocation(), sound, 1, 1);
    }
}
