package zedly.zenchantments.enchantments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Utilities;
import zedly.zenchantments.Zenchantment;

import static org.bukkit.Material.MACE;
import static org.bukkit.damage.DamageType.MACE_SMASH;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Groundbreaker extends Zenchantment {
    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !(event.getEntity() instanceof LivingEntity target)
            || event.getDamageSource().getDamageType() != MACE_SMASH
            || player.getInventory().getItem(slot).getType() != MACE) {
            return false;
        }

        final int duration = 40 + level * 20;
        final int amplifier = Math.min(3, level - 1);
        Utilities.addPotionEffect(target, PotionEffectType.MINING_FATIGUE, duration, amplifier);
        Utilities.addPotionEffect(target, PotionEffectType.SLOWNESS, duration, amplifier);
        return true;
    }
}