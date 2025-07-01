package zedly.zenchantments.arrows;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public final class LightningArrow extends ZenchantedArrow {

    public LightningArrow(
        final @NotNull Projectile entity,
        final int level,
        final double power
    ) {
        super(entity, level, power);
    }

    @Override
    public void onImpactEntity(@NotNull ProjectileHitEvent event) {
        onImpact(event);
    }

    @Override
    public void onImpact(final @NotNull ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            Entity target = event.getHitEntity();
            target.getWorld().strikeLightning(target.getLocation());
        } else if (event.getHitBlock() != null) {
            Block target = event.getHitBlock();
            target.getWorld().strikeLightning(target.getLocation());
        }
    }
}
