package de.tert0.btb.commands;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BTBRoleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Component.text("This command can only be executed by a player", NamedTextColor.RED));
            return true;
        }

        if(args.length != 1 && args.length != 2) {
            commandSender.sendMessage(Component.text("usage: /" + label + " <role> [player]"));
            return true;
        }

        Role role;
        try {
            role = Role.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(
                    Component.text("Invalid role (", NamedTextColor.RED)
                            .append(
                                    Component.text(
                                            Arrays.stream(Role.values()).map(Enum::toString).collect(Collectors.joining(", ")),
                                            NamedTextColor.GOLD
                                    )
                            )
                            .append(Component.text(")", NamedTextColor.RED))
            );
            return true;
        }

        Player target;
        if(args.length == 2) {
            target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                commandSender.sendMessage(
                        Component.text("Could not find player: ", NamedTextColor.RED)
                                .append(Component.text(args[1], NamedTextColor.GOLD))
                );
                return true;
            }

            if(!target.equals(commandSender) && !commandSender.hasPermission("btb.role.admin")) {
                commandSender.sendMessage(Component.text("Permission denied", NamedTextColor.RED));
                return true;
            }
        } else {
            target = (Player) commandSender;
        }

        if(BTB.getPlugin().gameState != GameState.Lobby) {
            if(!BTB.getPlugin().game.isSpectator(target)) {
                commandSender.sendMessage(Component.text("You can not select a role!", NamedTextColor.RED));
                return true;
            }
        }

        BTB.getPlugin().roles.put(target.getUniqueId(), role);
        BTB.getPlugin().updatePlayerName(target);
        commandSender.sendMessage(Component.text("You have successfully selected ", NamedTextColor.GREEN).append(Component.text(role.name, NamedTextColor.GOLD)));
        return true;
    }
}
