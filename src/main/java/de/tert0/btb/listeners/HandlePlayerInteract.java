package de.tert0.btb.listeners;

import de.tert0.btb.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

public class HandlePlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(BTB.getPlugin().gameState != GameState.Playing) return;

        Player player = event.getPlayer();
        if(event.getItem() != null && event.getItem().getPersistentDataContainer().has(CustomItem.KEY, PersistentDataType.STRING)) {
            CustomItem itemType = CustomItem.getById(event.getItem().getPersistentDataContainer().get(CustomItem.KEY, PersistentDataType.STRING));
            if(itemType == null) {
                BTB.getPlugin().getLogger().warning("Custom item with id " + event.getItem().getPersistentDataContainer().get(CustomItem.KEY, PersistentDataType.STRING) + " not found");
                return;
            }

            if(!itemType.role.equals(Role.getByPlayer(player))) {
                player.sendActionBar(
                        Component.text("Only players of the role ", NamedTextColor.RED)
                                .append(Component.text(itemType.role.name, NamedTextColor.GOLD))
                                .append(Component.text(" can use this item!", NamedTextColor.RED))
                );
                return;
            }
            switch(itemType) {
                case CustomItem.Lighter -> {
                    if(!event.getAction().isRightClick()) return;

                    RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 50, target_entity -> {
                        if(target_entity instanceof Player target) {
                            return !BTB.getPlugin().game.isSpectator(target) && Team.getByPlayer(target) != Team.getByPlayer(player);
                        }
                        return false;
                    });
                    if(result != null && result.getHitEntity() != null && result.getHitEntity() instanceof Player target) {
                        LightningStrike lightning = player.getWorld().strikeLightning(target.getLocation());
                        lightning.setCausingPlayer(player);
                    } else {
                        player.sendActionBar(Component.text("No target found", NamedTextColor.RED));
                    }
                }
                case InfinitePotion -> {
                }
                case CustomItem.DamageBarrier -> {

                }
                case null -> {
                }
            }
        }
    }
}
