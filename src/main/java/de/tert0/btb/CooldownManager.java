package de.tert0.btb;

import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final Map<Pair<UUID, CustomItem>, Instant> cooldowns;

    public CooldownManager() {
        cooldowns = new HashMap<>();
    }

    public void addCooldown(UUID uuid, CustomItem item) {
        cooldowns.put(Pair.of(uuid, item), Instant.now());
    }

    public int getCooldown(UUID uuid, CustomItem item) {
        Instant lastUsed = this.cooldowns.get(Pair.of(uuid, item));
        if (lastUsed == null) {
            return 0;
        }
        int time = (int) (Instant.now().getEpochSecond() - lastUsed.getEpochSecond());
        if(time >= item.cooldown) {
            return 0;
        }
        return item.cooldown - time;
    }
}
