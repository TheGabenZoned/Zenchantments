package zedly.zenchantments.enchantments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Zenchantment;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Hook extends Zenchantment {
    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !(event.getEntity() instanceof LivingEntity target)
            || !SpearCombat.isSpear(player.getInventory().getItem(slot))) {
            return false;
        }

        Vector pull = player.getLocation().toVector().subtract(target.getLocation().toVector());
        pull.setY(0);
        if (pull.lengthSquared() > 0.01) {
            target.setVelocity(target.getVelocity().multiply(0.35).add(pull.normalize().multiply(0.5 * level)));
        }
        return true;
    }
}
