package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.Team;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class HandleEnderchest implements Listener {
    @EventHandler
    public void onOpenEnderchest(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() != Material.ENDER_CHEST) return;

        event.setCancelled(true);
        Team team = Team.getByPlayer(event.getPlayer());
        if(team == null) return;
        Inventory inv = BTB.getPlugin().teamChests.get(team);
        if(inv == null) return;
        event.getPlayer().openInventory(inv);
    }
}
