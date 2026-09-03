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

import static org.bukkit.Material.MACE;
import static org.bukkit.damage.DamageType.MACE_SMASH;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class SeismicSlam extends Zenchantment {
    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || event.getDamageSource().getDamageType() != MACE_SMASH
            || player.getInventory().getItem(slot).getType() != MACE) {
            return false;
        }

        final LivingEntity impactTarget = (LivingEntity) event.getEntity();
        final double radius = Math.min(12, level * 4);
        final double damage = event.getFinalDamage() * 0.25;

        for (final LivingEntity target : impactTarget.getWorld()
            .getNearbyEntities(impactTarget.getLocation(), radius, radius, radius).stream()
            .filter(entity -> entity instanceof LivingEntity)
            .map(entity -> (LivingEntity) entity)
            .filter(target -> target.getLocation().distanceSquared(impactTarget.getLocation()) <= radius * radius)
            .filter(target -> target != impactTarget && target != player)
            .toList()) {
            if (damage > 0) {
                target.damage(damage, player);
            }

            final Vector knockback = target.getLocation().toVector().subtract(impactTarget.getLocation().toVector());
            knockback.setY(0);
            if (knockback.lengthSquared() > 0.01) {
                target.setVelocity(target.getVelocity().multiply(0.35).add(knockback.normalize().multiply(0.8 + level * 0.2)).setY(0.45 + level * 0.05));
            }
        }

        return true;
    }
}