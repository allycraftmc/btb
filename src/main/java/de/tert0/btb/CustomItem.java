package de.tert0.btb;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

public enum CustomItem {
    Lighter("lighter", Role.Knight),
    InfinitePotion("infinite_potion", Role.Healer),
    DamageBarrier("damage_barrier", Role.Tank),;

    public final String id;
    public final Role role;
    CustomItem(String id, Role role) {
        this.id = id;
        this.role = role;
    }

    public static final NamespacedKey KEY = new NamespacedKey(BTB.getPlugin(), "custom_item");

    public static CustomItem getById(String id) {
        for (CustomItem item : values()) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }
}
