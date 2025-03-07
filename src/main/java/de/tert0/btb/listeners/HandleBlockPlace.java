package de.tert0.btb.listeners;

import de.tert0.btb.CustomItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HandleBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getPlayer().getWorld().getName().equals("world")) {
            e.setCancelled(true);
            return;
        }

        if(e.getItemInHand().getPersistentDataContainer().has(CustomItem.KEY)) {
            e.setCancelled(true);
        }
    }
}
