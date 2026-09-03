package zedly.zenchantments.enchantments;

import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Entity;
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
public final class Skewer extends Zenchantment {
    private static final ThreadLocal<Boolean> FOLLOW_UP = ThreadLocal.withInitial(() -> false);

    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (FOLLOW_UP.get()
            || !(event.getDamager() instanceof Player player)
            || !(event.getEntity() instanceof LivingEntity firstTarget)
            || !SpearCombat.isSpear(player.getInventory().getItem(slot))) {
            return false;
        }

        Vector direction = player.getLocation().getDirection().setY(0);
        if (direction.lengthSquared() < 0.01) {
            return false;
        }
        direction.normalize();
        List<Entity> targets = firstTarget.getNearbyEntities(2.6, 1.5, 2.6).stream()
            .filter(entity -> entity instanceof LivingEntity && entity != player && entity != firstTarget)
            .filter(entity -> {
                Vector offset = entity.getLocation().toVector().subtract(firstTarget.getLocation().toVector());
                double distance = offset.dot(direction);
                return distance > 0.2 && distance <= 2.6 && offset.clone().subtract(direction.clone().multiply(distance)).lengthSquared() <= 1.0;
            })
            .sorted(Comparator.comparingDouble(entity -> entity.getLocation().toVector().subtract(firstTarget.getLocation().toVector()).dot(direction)))
            .limit(Math.max(0, level))
            .toList();

        FOLLOW_UP.set(true);
        try {
            for (Entity target : targets) {
                ((LivingEntity) target).damage(event.getFinalDamage(), player);
            }
        } finally {
            FOLLOW_UP.set(false);
        }
        return !targets.isEmpty();
    }
}
