package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HandlePlayerDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player player)) {
            return;
        }
        if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            return;
        }

        if(player.getWorld().getName().equals("world")) {
            e.setCancelled(true);
        }

        if(BTB.getPlugin().deadPlayers.contains(player)) {
            e.setCancelled(true);
        }
    }
}
