package de.tert0.btb.listeners;

import de.tert0.btb.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.TileState;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

            if(!event.getAction().isRightClick()) return;

            if(event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof TileState) {
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }

            if(!itemType.role.equals(Role.getByPlayer(player))) {
                player.sendActionBar(
                        Component.text("Only players of the role ", NamedTextColor.RED)
                                .append(Component.text(itemType.role.name, NamedTextColor.GOLD))
                                .append(Component.text(" can use this item!", NamedTextColor.RED))
                );
                event.setCancelled(true);
                return;
            }

            int cooldown = BTB.getPlugin().cooldownManager.getCooldown(player.getUniqueId(), itemType);
            if(cooldown != 0) {
                player.sendActionBar(
                        Component.text("You cannot use it now. Cooldown: ", NamedTextColor.RED)
                                .append(Component.text(cooldown, NamedTextColor.GOLD))
                );
                event.setCancelled(true);
                return;
            }

            switch(itemType) {
                case CustomItem.Lighter -> {
                    RayTraceResult result = player.getWorld().rayTrace(
                            player.getEyeLocation(), player.getLocation().getDirection(),
                            50, FluidCollisionMode.NEVER, true,
                            0.0, target_entity -> {
                                if(target_entity instanceof Player target) {
                                    return !BTB.getPlugin().game.isSpectator(target) && Team.getByPlayer(target) != Team.getByPlayer(player);
                                }
                                return false;
                            },
                            null
                    );
                    if(result != null && result.getHitEntity() != null && result.getHitEntity() instanceof Player target) {
                        LightningStrike lightning = player.getWorld().strikeLightning(target.getLocation());
                        lightning.setCausingPlayer(player);
                        BTB.getPlugin().cooldownManager.addCooldown(player.getUniqueId(), itemType);
                    } else {
                        player.sendActionBar(Component.text("No target found", NamedTextColor.RED));
                    }
                }
                case InfinitePotion -> {
                    EquipmentSlot hand = event.getHand();
                    ItemStack item = event.getItem().clone();
                    if(hand == null) return;
                    Bukkit.getScheduler().runTaskLater(BTB.getPlugin(), task -> {
                        if(!player.isValid()) return;
                        if(player.getEquipment().getItem(hand).isEmpty()) {
                            player.getEquipment().setItem(hand, item);
                            BTB.getPlugin().cooldownManager.addCooldown(player.getUniqueId(), itemType);
                        }
                    }, 1);
                }
                case CustomItem.DamageBarrier -> {
                    if(!event.getAction().isRightClick()) return;

                    // TODO play sound

                    int duration = 5; // s
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20*duration, 255, false, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*duration, 1, false, false, true));
                    BTB.getPlugin().cooldownManager.addCooldown(player.getUniqueId(), itemType);
                }
            }
        }
    }
}
