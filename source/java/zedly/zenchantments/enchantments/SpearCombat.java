package zedly.zenchantments.enchantments;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.Tool;

public final class SpearCombat {
    private SpearCombat() {
    }

    public static boolean isSpear(final ItemStack item) {
        return item != null && Tool.SPEAR.contains(item.getType());
    }

    public static boolean isCharge(final Player player, final Entity target) {
        Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector());
        toTarget.setY(0);
        Vector velocity = player.getVelocity().clone();
        velocity.setY(0);
        return toTarget.lengthSquared() > 0.01
            && velocity.lengthSquared() > 0.0225
            && velocity.normalize().dot(toTarget.normalize()) > 0.35;
    }

    public static boolean isMovingToward(final Entity target, final Player player) {
        Vector toPlayer = player.getLocation().toVector().subtract(target.getLocation().toVector());
        toPlayer.setY(0);
        Vector velocity = target.getVelocity().clone();
        velocity.setY(0);
        return toPlayer.lengthSquared() > 0.01
            && velocity.lengthSquared() > 0.0025
            && velocity.normalize().dot(toPlayer.normalize()) > 0.35;
    }

    public static int nearbyFormationMembers(final @NotNull Player player) {
        int members = 0;
        for (Entity entity : player.getNearbyEntities(6, 6, 6)) {
            if (members >= 3) {
                break;
            }
            if (entity instanceof Player nearbyPlayer
                && !nearbyPlayer.equals(player)
                && isSpear(nearbyPlayer.getInventory().getItemInMainHand())) {
                members++;
            } else if (entity instanceof Tameable pet && player.equals(pet.getOwner())) {
                members++;
            }
        }
        return members;
    }
}
