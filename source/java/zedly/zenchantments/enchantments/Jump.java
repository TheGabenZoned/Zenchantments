package zedly.zenchantments.enchantments;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;


@AZenchantment(runInSlots = Slots.ARMOR, conflicting = {})
public final class Jump extends Zenchantment {
    @Override
    public boolean onScan(final @NotNull Player player, final int level, final EquipmentSlot slot) {
        Utilities.addPotionEffect(player, PotionEffectType.JUMP_BOOST, 610, (int) Math.round(level * this.getPower()));
        return true;
    }
}
