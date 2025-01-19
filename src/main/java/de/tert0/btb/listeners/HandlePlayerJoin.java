package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class HandlePlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.setScoreboard(BTB.getPlugin().scoreboard);

        if(BTB.getPlugin().gameState == GameState.Playing || BTB.getPlugin().gameState == GameState.End) {
            if(e.getPlayer().hasPermission("btb.spectate")) {
                BTB.getPlugin().game.addSpectator(e.getPlayer());
            }
            else {
                e.getPlayer().kick(Component.text("Permission denied. Unknown error!", NamedTextColor.DARK_RED));
                e.joinMessage(null);
            }
            return;
        }
        if(!e.getPlayer().getWorld().getName().equals("world")) {
            e.getPlayer().teleport((Objects.requireNonNull(Bukkit.getWorld("world"))).getSpawnLocation());
        }

        BTB.getPlugin().game.resetPlayer(player);
        BTB.getPlugin().roles.put(player.getUniqueId(), Role.getRandomRole());
        BTB.getPlugin().updatePlayerName(player);


        player.sendMessage(MiniMessage.miniMessage().deserialize("""
                <green>Willkommen bei BTB</green>
                <white>Eure Aufgabe ist es so schnell wie möglich den Turm zu erklimmen und die Gegner auszuschalten.
                Während dem erklimmen könnt ihr <dark_red>Truhen</dark_red> finden die wichtige Items enthalten.</white>"""));
    }
}
