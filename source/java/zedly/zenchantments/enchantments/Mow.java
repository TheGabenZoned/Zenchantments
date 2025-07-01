package zedly.zenchantments.enchantments;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

import java.util.Objects;
import java.util.Random;

@AZenchantment(runInSlots = Slots.HANDS, conflicting = {})
public final class Mow extends Zenchantment {
    @Override
    public boolean onShear(final @NotNull PlayerShearEntityEvent event, final int level, final EquipmentSlot slot) {
        return this.shear(event, level, slot);
    }

    private boolean shear(final @NotNull PlayerShearEntityEvent event, final int level, final EquipmentSlot slot) {
        final int radius = (int) Math.round(level * this.getPower() + 2);
        final Player player = event.getPlayer();

        boolean shearedEntity = false;

        for (final Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if(entity == event.getEntity()) {
                continue;
            }
            if (entity instanceof Sheep sheep) {
                if (sheep.isAdult() && !sheep.isSheared() && sheep.getColor() != null) {

                    DyeColor color = sheep.getColor();
                    Material woolType = Material.getMaterial(color.name() + "_WOOL");
                    for (String item : Objects.requireNonNull(Objects.requireNonNull(event.getItem().getItemMeta()).getLore()).stream().toList() ) {
                            if (item.substring(2).trim().equalsIgnoreCase("Rainbow")) {
                                woolType = MaterialList.WOOL.getRandom();
                            }
                    }
                    if (woolType != null) {
                        sheep.setSheared(true);
                        sheep.getWorld().playSound(sheep.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);

                        int dropAmount = 1 + new Random().nextInt(3);
                        ItemStack drop = new ItemStack(woolType, dropAmount);
                        sheep.getWorld().dropItemNaturally(sheep.getLocation(), drop);

                        shearedEntity = true;
                    }
                }
            } else if (entity instanceof MushroomCow mooshroom) {
                if (mooshroom.isAdult()) {
                    mooshroom.getWorld().spawnParticle(Particle.EXPLOSION, mooshroom.getLocation().add(0, 1, 0), 1);
                    mooshroom.getWorld().playSound(mooshroom.getLocation(), Sound.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);

                    Cow cow = (Cow) mooshroom.getWorld().spawnEntity(mooshroom.getLocation(), EntityType.COW);
                    cow.setHealth(mooshroom.getHealth());
                    cow.setCustomName(mooshroom.getCustomName());
                    cow.setCustomNameVisible(mooshroom.isCustomNameVisible());
                    cow.setPersistent(mooshroom.isPersistent());
                    cow.setInvulnerable(mooshroom.isInvulnerable());
                    mooshroom.remove();
                    ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM, 5);
                    mooshroom.getWorld().dropItemNaturally(mooshroom.getLocation(), mushrooms);

                    shearedEntity = true;
                }
            }
        }

        return shearedEntity;
    }
}
