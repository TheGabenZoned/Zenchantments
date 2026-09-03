package zedly.zenchantments.enchantments;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Zenchantment;

import static org.bukkit.Material.MACE;
import static org.bukkit.damage.DamageType.MACE_SMASH;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Burrow extends Zenchantment {
    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !(event.getEntity() instanceof LivingEntity target)
            || event.getDamageSource().getDamageType() != MACE_SMASH
            || player.getInventory().getItem(slot).getType() != MACE) {
            return false;
        }

        Location destination = target.getLocation();
        for (int blocks = 0; blocks < Math.min(3, level); blocks++) {
            final Location below = destination.clone().subtract(0, 1, 0);
            if (!below.getBlock().isPassable() || !below.clone().add(0, 1, 0).getBlock().isPassable()) {
                break;
            }
            destination = below;
        }

        if (!destination.equals(target.getLocation())) {
            target.teleport(destination);
        }
        return true;
    }
}