package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class HandleSmithingInventory implements Listener {
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(e.getInventory() instanceof SmithingInventory inventory) {
            inventory.setInputTemplate(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
        }
    }

    @EventHandler
    public void onSmith(SmithItemEvent e) {
        if(e.getClickedInventory() == null) {
            BTB.getPlugin().getLogger().warning("Smithing Table Inventory is null");
            return;
        }
        e.getInventory().setInputTemplate(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getInventory() instanceof SmithingInventory inventory) {
            if(inventory.getInputTemplate() != null && inventory.getInputTemplate().getType().equals(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)) {
                inventory.setInputTemplate(null);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() instanceof SmithingInventory) {
            if(e.getSlot() == 0) {
                e.setCancelled(true);
            }
        }
    }
}
