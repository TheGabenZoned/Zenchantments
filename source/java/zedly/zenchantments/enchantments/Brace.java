package zedly.zenchantments.enchantments;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Zenchantment;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Brace extends Zenchantment {
    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !SpearCombat.isSpear(player.getInventory().getItem(slot))
            || !SpearCombat.isMovingToward(event.getEntity(), player)) {
            return false;
        }

        event.setDamage(event.getDamage() * (1 + 0.15 * level + 0.05));
        return true;
    }
}
