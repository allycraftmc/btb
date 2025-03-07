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
                <green>Willkommen bei BTB!</green>
                <bold><red>Das Ziel:</red></bold> So schnell wie möglich nach oben kommen und die anderen Teams ausschalten.
                Um das ganze Team auschalten zu können müssen alle von diesem Team tot sein. <red>Respawn nach 20 sekunden</red>

                Kleiner Tipp:
                Fange am besten an
                Holz durch Knochenmehl und Setzlinge zu bekommen und baue Leitern um an <gray>Eisen</gray> und <aqua>Diamanten</aqua> zu kommen.

                Du kannst dich für eine der drei Rollen entscheiden.
                Die Fähigkeiten der Rollen befinden sich in der Hotbar und können mit dem <light_purple>Rechtsklick</light_purple> ausgelöst werden.
                Die Fähigkeiten:
                <bold><blue>Tank:</blue></bold> Unbesiegbar für 5 Sekunden
                <bold><green>Healer:</green></bold> Wirft starke Heiltränke
                <bold><red>Knight</red></bold>: Verschießt einen Blitz auf einen Spieler (50 Block Radius)"""));
    }
}
