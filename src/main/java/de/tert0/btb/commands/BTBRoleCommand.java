package de.tert0.btb.commands;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("This command can only be executed by a player", NamedTextColor.RED));
            return true;
        }
        if(BTB.getPlugin().gameState != GameState.Lobby) {
            if(!BTB.getPlugin().game.isSpectator(player)) {
                commandSender.sendMessage(Component.text("You can not select a role!", NamedTextColor.RED));
                return true;
            }
        }

        if(args.length != 1) {
            commandSender.sendMessage(Component.text("usage: /" + label + " <role>"));
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

        BTB.getPlugin().roles.put(player.getUniqueId(), role);
        commandSender.sendMessage(Component.text("You have successfully selected ", NamedTextColor.GREEN).append(Component.text(role.name, NamedTextColor.GOLD)));
        return true;
    }
}
