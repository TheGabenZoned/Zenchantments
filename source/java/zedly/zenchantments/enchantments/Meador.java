package zedly.zenchantments.enchantments;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

@AZenchantment(runInSlots = Slots.ARMOR, conflicting = {Weight.class, Speed.class, Jump.class})
public final class Meador extends Zenchantment {
    @Override
    public boolean onScan(final @NotNull Player player, final int level, final EquipmentSlot slot) {
        //final float speed = (float) Math.min(0.5f + level * this.getPower() * 0.05f, 1);
        final float speed = (float) Math.min((0.2f * this.getPower()) + 0.2f, 1);

        player.setWalkSpeed(speed);
        player.setFlySpeed(speed);

        player.setMetadata("ze.speed", new FixedMetadataValue(ZenchantmentsPlugin.getInstance(), System.currentTimeMillis()));

        Utilities.addPotionEffect(player, PotionEffectType.JUMP_BOOST, 610, (int) Math.round(this.getPower() * level + 2));

        return true;
    }
}
