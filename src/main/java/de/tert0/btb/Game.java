package de.tert0.btb;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private final BTB plugin;
    private final AtomicBoolean starting = new AtomicBoolean(false);

    private final Set<Player> spectators = new HashSet<>();
    public Game(BTB plugin) {
        this.plugin = plugin;
    }

    boolean can_start() {
        boolean teamsFull = Arrays.stream(Team.values()).allMatch(team -> plugin.teams.get(team).size() == 2);
        return teamsFull && plugin.gameState == GameState.Lobby;
    }

    public void try_start() {
        if(!can_start()) return;

        starting.set(true);

        AtomicInteger count = new AtomicInteger(10);

        plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
            if(!can_start()) {
                task.cancel();
                plugin.getServer().broadcast(Component.text("Spielstart abgebrochen!", NamedTextColor.DARK_RED));
                starting.set(false);
                return;
            }

            if(count.get() <= 0) {
                plugin.getServer().broadcast(Component.text("Spiel beginnt!", NamedTextColor.GREEN));
                starting.set(false);
                start();
                task.cancel();
                return;
            }

            plugin.getServer().broadcast(
                    Component.text("Das Spiel startet in ", NamedTextColor.YELLOW)
                            .append(Component.text(count.get(), NamedTextColor.RED))
                            .append(Component.text(" sekunden", NamedTextColor.YELLOW))
            );

            count.addAndGet(-1);
        }, 0, 20);
    }

    public boolean start() {
        if(starting.get()) return false;
        Bukkit.getOnlinePlayers().forEach(p -> {
            Team team = Team.getByPlayer(p);
            if(team == null) {
                if(p.hasPermission("btb.spectate")) {
                    this.addSpectator(p);
                } else {
                    p.kick(Component.text("You are not allowed to spectate the game!", NamedTextColor.DARK_RED));
                }
            }
        });

        BTB.getPlugin().objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for(Map.Entry<Team, List<Player>> entry : plugin.teams.entrySet()) {
            for(Player player : entry.getValue()) {
                this.resetPlayer(player);
                player.teleport(entry.getKey().getSpawnPoint(plugin.btbWorld));
            }
        }

        plugin.updateTeamStates();

        plugin.gameState = GameState.Playing;

        return true;
    }


    public void addSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(plugin.btbWorld.getSpawnLocation());
        this.spectators.add(player);
    }

    public void removeSpectator(Player player) {
        if(!this.isSpectator(player)) return;
        this.spectators.remove(player);
    }

    public boolean isSpectator(Player player) {
        return this.spectators.contains(player);
    }

    public void resetPlayer(Player player) {
        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setExp(0.0f);
        player.setHealth(20.0f);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
    }
}
