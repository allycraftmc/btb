package de.tert0.btb.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HandlePlayerInteractBlock implements Listener {
    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(e.getPlayer().getWorld().getName().equals("world")) {
            if(e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) && e.getAction().isRightClick()) return;
            e.setCancelled(true);
        }
    }
}
