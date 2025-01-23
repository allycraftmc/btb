package de.tert0.btb;

import org.bukkit.NamespacedKey;

public enum CustomItem {
    Lighter("lighter", Role.Knight, 16),
    InfinitePotion("infinite_potion", Role.Healer, 8),
    DamageBarrier("damage_barrier", Role.Tank, 24);

    public final String id;
    public final Role role;
    public final int cooldown;
    CustomItem(String id, Role role, int cooldown) {
        this.id = id;
        this.role = role;
        this.cooldown = cooldown;
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
