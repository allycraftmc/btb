package de.tert0.btb.commands;

import de.tert0.btb.BTB;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HazardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("btb.hazard")){
            sender.sendMessage(Component.text("Permission denied", NamedTextColor.RED));
            return true;
        }

        if(args.length != 1){
            sender.sendMessage(Component.text("usage: /" + label + " <hazard>"));
            return true;
        }

        if(!BTB.getPlugin().hazardManager.hasHazard(args[0])){
            sender.sendMessage(Component.text("Hazard does not exist", NamedTextColor.RED));
            return true;
        }

        if(BTB.getPlugin().hazardManager.isRunning(args[0])){
            sender.sendMessage(Component.text("Hazard is already running", NamedTextColor.RED));
            return true;
        }

        boolean result = BTB.getPlugin().hazardManager.startHazard(args[0]);
        if(result) {
            sender.sendMessage(Component.text("Started Hazard", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text("Failed to start hazard", NamedTextColor.RED));
        }

        return true;
    }
}
