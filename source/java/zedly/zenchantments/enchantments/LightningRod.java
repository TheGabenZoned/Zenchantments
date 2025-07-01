package zedly.zenchantments.enchantments;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;
import zedly.zenchantments.arrows.LightningArrow;
import zedly.zenchantments.arrows.ZenchantedArrow;

@AZenchantment(runInSlots = Slots.HANDS, conflicting = {})
public final class LightningRod extends Zenchantment {
    @Override
    public boolean onEntityShootBow(final @NotNull EntityShootBowEvent event, final int level, final EquipmentSlot slot) {
        final AbstractArrow eventArrow = (AbstractArrow) event.getProjectile();
        final LightningArrow arrow = new LightningArrow(eventArrow, level, this.getPower());
        ZenchantedArrow.addZenchantedArrowToArrowEntity(eventArrow, arrow, (Player) event.getEntity());
        return true;
    }

    @Override
    public boolean onProjectileLaunch(final @NotNull ProjectileLaunchEvent event, final int level, final EquipmentSlot slot) {
        if(event.getEntity().getType() != EntityType.TRIDENT) {
            return false;
        }
        final LightningArrow arrow = new LightningArrow(event.getEntity(), level, getPower());
        ZenchantedArrow.addZenchantedArrowToArrowEntity(event.getEntity(), arrow, (Player) event.getEntity().getShooter());
        return true;
    }
}
