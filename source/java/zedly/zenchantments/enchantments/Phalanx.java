package zedly.zenchantments.enchantments;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.AZenchantment;
import zedly.zenchantments.Slots;
import zedly.zenchantments.Zenchantment;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Phalanx extends Zenchantment {
    private static final UUID RESISTANCE_MODIFIER_ID = UUID.fromString("8f1d6d1d-9c8a-4e06-a2c5-20f3e1ecf126");

    @Override
    public boolean onEntityHit(final @NotNull EntityDamageByEntityEvent event, final int level, final EquipmentSlot slot) {
        if (!(event.getDamager() instanceof Player player)
            || !SpearCombat.isSpear(player.getInventory().getItem(slot))
            || !SpearCombat.isCharge(player, event.getEntity())) {
            return false;
        }

        int members = SpearCombat.nearbyFormationMembers(player);
        if (members == 0) {
            return false;
        }

        event.setDamage(event.getDamage() * (1 + members * 0.05));
        AttributeInstance resistance = player.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (resistance != null) {
            resistance.getModifiers().stream()
                .filter(modifier -> RESISTANCE_MODIFIER_ID.equals(modifier.getUniqueId()))
                .findFirst()
                .ifPresent(resistance::removeModifier);
            resistance.addModifier(new AttributeModifier(
                RESISTANCE_MODIFIER_ID,
                "Zenchantments Phalanx",
                members * 0.05,
                AttributeModifier.Operation.ADD_NUMBER
            ));
            player.getServer().getScheduler().runTaskLater(
                zedly.zenchantments.ZenchantmentsPlugin.getInstance(),
                () -> {
                    resistance.getModifiers().stream()
                        .filter(candidate -> RESISTANCE_MODIFIER_ID.equals(candidate.getUniqueId()))
                        .findFirst()
                        .ifPresent(resistance::removeModifier);
                },
                2L
            );
        }
        return true;
    }
}
