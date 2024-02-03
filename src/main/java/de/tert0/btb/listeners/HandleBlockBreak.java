package de.tert0.btb.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.lang.reflect.Field;

public class HandleBlockBreak implements Listener {
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if(!e.getBlock().getWorld().getName().equals("world")) return;
        e.setCancelled(true);
    }
}
