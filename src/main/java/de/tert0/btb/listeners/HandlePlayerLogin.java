package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class HandlePlayerLogin implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if(BTB.getPlugin().gameState == GameState.Initialization) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("Server is not ready to start!", NamedTextColor.RED));
            return;
        }
        if(BTB.getPlugin().gameState == GameState.Playing || BTB.getPlugin().gameState == GameState.End) {
            if(e.getPlayer().hasPermission("btb.spectate")) {
                BTB.getPlugin().addSpectator(e.getPlayer());
                return;
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("Cannot join running game!", NamedTextColor.RED));
        }
    }
}
