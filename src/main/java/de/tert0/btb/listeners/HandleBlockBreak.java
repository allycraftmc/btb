package de.tert0.btb.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class HandleBlockBreak implements Listener {
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if(e.getBlock().getWorld().getName().equals("world")) {
            e.setCancelled(true);
            return;
        }

        if(e.getBlock().getType().equals(Material.SMITHING_TABLE)) {
            e.setCancelled(true);
        }
    }
}
