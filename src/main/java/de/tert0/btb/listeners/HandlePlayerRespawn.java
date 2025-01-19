package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.Constants;
import de.tert0.btb.GameState;
import de.tert0.btb.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class HandlePlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if(BTB.getPlugin().gameState == GameState.Lobby) {
            e.setRespawnLocation((Objects.requireNonNull(Bukkit.getWorld("world"))).getSpawnLocation());
            return;
        }

        if(BTB.getPlugin().gameState == GameState.Playing) {
            Team team = Team.getByPlayer(e.getPlayer());
            assert team != null;
            if(!BTB.getPlugin().teamStates.get(team)) {
                e.setRespawnLocation(team.getSpawnPoint(BTB.getPlugin().btbWorld));
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else if(BTB.getPlugin().deadPlayers.contains(e.getPlayer())) {
                e.setRespawnLocation(Constants.RESPAWN_CHAMBER);
            } else {
                e.setRespawnLocation(team.getSpawnPoint(BTB.getPlugin().btbWorld));
                BTB.getPlugin().game.setupPlayer(e.getPlayer());
            }
        } else if(BTB.getPlugin().gameState == GameState.End) {
            Team team = Team.getByPlayer(e.getPlayer());
            assert team != null;

            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.setRespawnLocation(team.getSpawnPoint(BTB.getPlugin().btbWorld));
        }
    }
}
