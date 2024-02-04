package de.tert0.btb.listeners;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class HandlePlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!e.getPlayer().getWorld().getName().equals("world")) {
            e.getPlayer().teleport((Objects.requireNonNull(Bukkit.getWorld("world"))).getSpawnLocation());
        }

        Player player = e.getPlayer();

        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setExp(0.0f);
        player.setHealth(20.0f);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);


        player.sendMessage(MiniMessage.miniMessage().deserialize("<rainbow>Hier könnte eine Erklärung stehen!</rainbow><newline><green>TODO</green>"));
        // TODO explain game
    }
}
