package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Map;

public class HandlePlayerLeave implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        for(Map.Entry<Team, List<Player>> entry : BTB.getPlugin().teams.entrySet()) {
            entry.getValue().remove(e.getPlayer());
        }
        if(BTB.getPlugin().gameState == GameState.Playing) BTB.getPlugin().updateTeamStates();

        if(BTB.getPlugin().gameState != GameState.Lobby) {
            if(Bukkit.getOnlinePlayers().size() == 1) {
                BTB.getPlugin().getLogger().info("All players left: Restarting Server");
                Bukkit.shutdown();
            }
        }
    }
}
