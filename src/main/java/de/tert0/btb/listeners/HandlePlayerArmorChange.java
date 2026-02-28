package de.tert0.btb.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import de.tert0.btb.Role;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

public class HandlePlayerArmorChange implements Listener {
    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent e) {
        Role role = Role.getByPlayer(e.getPlayer());
        e.getPlayer().getEquipment().getItem(e.getSlot()).editMeta(itemMeta -> {
            if(itemMeta instanceof ArmorMeta armorMeta) {
                armorMeta.setTrim(new ArmorTrim(role.trimMaterial, role.trimPattern));
            }
        });
    }
}
