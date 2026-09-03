package zedly.zenchantments.enchantments;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Zenchantment;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Momentum extends Zenchantment {
    private static final long RESET_AFTER_MILLIS = 1500;
    private static final Map<UUID, StackState> STACKS = new ConcurrentHashMap<>();

    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !SpearCombat.isSpear(player.getInventory().getItem(slot))) {
            return false;
        }

        StackState state = STACKS.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        if (state != null && now - state.lastCharge > RESET_AFTER_MILLIS) {
            STACKS.remove(player.getUniqueId());
            state = null;
        }

        if (state != null && state.stacks > 0) {
            event.setDamage(event.getDamage() * (1 + state.stacks * 0.05));
        }

        if (!SpearCombat.isCharge(player, event.getEntity())) {
            STACKS.remove(player.getUniqueId());
            return state != null;
        }

        int maximum = Math.min(4, level + 1);
        int stacks = state == null ? 1 : Math.min(maximum, state.stacks + 1);
        STACKS.put(player.getUniqueId(), new StackState(stacks, now));
        player.setVelocity(player.getVelocity().multiply(1 + stacks * 0.05));
        return true;
    }

    private record StackState(int stacks, long lastCharge) {
    }
}
