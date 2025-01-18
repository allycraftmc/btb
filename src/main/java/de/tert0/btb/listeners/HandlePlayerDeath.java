package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.CustomItem;
import de.tert0.btb.GameState;
import de.tert0.btb.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

public class HandlePlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(BTB.getPlugin().gameState != GameState.Playing) return;
        if(BTB.getPlugin().deadPlayers.contains(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        BTB.getPlugin().deadPlayers.add(e.getPlayer());

        BTB.getPlugin().updateTeamStates();

        e.getDrops().removeIf(item -> item.getPersistentDataContainer().has(CustomItem.KEY));

        AtomicInteger count = new AtomicInteger(25);
        BTB.getPlugin().getServer().getScheduler().runTaskTimer(BTB.getPlugin(), task -> {
            if(!e.getPlayer().isOnline()) {
                task.cancel();
                return;
            }
            count.addAndGet(-1);

            Team team = Team.getByPlayer(e.getPlayer());

            if(team != null) {
                if(!BTB.getPlugin().teamStates.get(team)) {
                    Component message = Component.text("Dein Team ist tot!", NamedTextColor.DARK_RED);
                    e.getPlayer().showTitle(Title.title(message, Component.text("")));
                    e.getPlayer().sendMessage(message);
                    e.getPlayer().teleport(team.getSpawnPoint(BTB.getPlugin().btbWorld));
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    task.cancel();
                    return;
                }
            }

            if(count.get() <= 0) {
                BTB.getPlugin().deadPlayers.remove(e.getPlayer());
                Component message = Component.text("Du bist respawnt!", NamedTextColor.GREEN);
                e.getPlayer().showTitle(Title.title(message, Component.text("")));
                e.getPlayer().sendMessage(message);
                if(team != null) {
                    e.getPlayer().teleport(team.getSpawnPoint(BTB.getPlugin().btbWorld));
                } else {
                    BTB.getPlugin().getLogger().warning("Could not get Team of Player: " + e.getPlayer().getName());
                }
                task.cancel();
                return;
            }
            e.getPlayer().showTitle(Title.title(Component.text("Du respawnst in"), Component.text(count.get() + "s")));
        }, 0, 20);
    }
}
