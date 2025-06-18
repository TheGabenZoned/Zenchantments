package zedly.zenchantments.enchantments;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

import static org.bukkit.potion.PotionEffectType.HASTE;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Haste extends Zenchantment {
    @Override
    public boolean onScan(final @NotNull Player player, final int level, final EquipmentSlot slot) {
        Utilities.addPotionEffect(player, HASTE, 610, (int) Math.round(level * this.getPower()));
        player.setMetadata("ze.haste", new FixedMetadataValue(ZenchantmentsPlugin.getInstance(), System.currentTimeMillis()));
        return false;
    }
}
