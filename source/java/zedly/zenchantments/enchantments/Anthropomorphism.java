package zedly.zenchantments.enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;
import zedly.zenchantments.task.EffectTask;
import zedly.zenchantments.task.Frequency;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.bukkit.Material.*;
import static org.bukkit.event.block.Action.*;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {Pierce.class, Switch.class})
public final class Anthropomorphism extends Zenchantment {
    public static final Map<Entity, Pair<Double, Vector>> ATTACK_BLOCKS = new HashMap<>();
    public static final Map<Entity, Entity>               IDLE_BLOCKS   = new HashMap<>();
    private static final List<Entity> VORTEX    = new ArrayList<>();
    private static boolean fallBool = false;

    @EffectTask(Frequency.MEDIUM_HIGH)
    public static void removeOldBlocks() {
        Iterator<Entity> iterator = IDLE_BLOCKS.keySet().iterator();
        while (iterator.hasNext()) {
            Entity block = iterator.next();
            if (!block.isValid()) {
                iterator.remove();
            }
        }

        iterator = ATTACK_BLOCKS.keySet().iterator();
        while (iterator.hasNext()) {
            Entity block = iterator.next();
            if (!block.isValid() || block.getTicksLived() > 100 || isTooFarFromOwner(block)) {
                block.remove();
                iterator.remove();
            }
        }
    }

    @EffectTask(Frequency.HIGH)
    public static void moveBlocks() {
        // Move aggressive Anthropomorphism Blocks towards a target & attack.
        final Iterator<Entity> iterator = ATTACK_BLOCKS.keySet().iterator();
        while (iterator.hasNext()) {
            final Entity blockEntity = iterator.next();
            if (!blockEntity.isValid() || blockEntity.getTicksLived() > 100 || isTooFarFromOwner(blockEntity)) {
                blockEntity.remove();
                iterator.remove();
                continue;
            }

            final Entity target = blockEntity.getNearbyEntities(7, 7, 7).stream()
                .filter(entity -> entity instanceof Monster)
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(blockEntity.getLocation())))
                .orElse(null);
            final Vector playerDir = ATTACK_BLOCKS.get(blockEntity).getValue();
            if (target == null) {
                moveBlock(blockEntity, playerDir.clone().normalize().multiply(0.6));
                continue;
            }

            moveBlock(blockEntity,
                target.getLocation()
                    .add(playerDir.clone().multiply(.75))
                    .subtract(blockEntity.getLocation())
                    .toVector()
                    .multiply(0.25)
            );

            final LivingEntity targetEntity = (LivingEntity) target;
            if (targetEntity.getLocation().distanceSquared(blockEntity.getLocation()) < 1.44
                && blockEntity.hasMetadata("ze.anthrothrower")) {
                final Player attacker = (Player) blockEntity.getMetadata("ze.anthrothrower").get(0).value();

                if (targetEntity.getNoDamageTicks() == 0) {
                    final boolean result = WorldInteractionUtil.attackEntity(
                        targetEntity,
                        requireNonNull(attacker),
                        2.0 * ATTACK_BLOCKS.get(blockEntity).getKey()
                    );
                    if (result) {
                        targetEntity.setNoDamageTicks(0);
                        iterator.remove();
                        blockEntity.remove();
                    }
                }
            }
        }

        // Move passive Anthropomorphism Blocks around
        fallBool = !fallBool;

        for (final Entity block : IDLE_BLOCKS.keySet()) {
            if (!VORTEX.contains(IDLE_BLOCKS.get(block))) {
                continue;
            }

            final Location location = IDLE_BLOCKS.get(block).getLocation();
            final Vector vector;

            if (!Objects.equals(block.getLocation().getWorld(), IDLE_BLOCKS.get(block).getLocation().getWorld())) {
                continue;
            }

            if (fallBool && block.getLocation().distance(IDLE_BLOCKS.get(block).getLocation()) < 10) {
                vector = block.getLocation().subtract(location).toVector();
            } else {
                final double x = 6f * Math.sin(block.getTicksLived() / 10f);
                final double z = 6f * Math.cos(block.getTicksLived() / 10f);
                final Location tLoc = location.clone();
                tLoc.setX(tLoc.getX() + x);
                tLoc.setZ(tLoc.getZ() + z);
                vector = tLoc.subtract(block.getLocation()).toVector();
            }

            vector.multiply(.05);
            boolean close = false;

            for (int y = -3; y < 0; y++) {
                if (block.getLocation().getBlock().getRelative(0, y, 0).getType() != AIR) {
                    close = true;
                }
            }

            if (close) {
                vector.setY(Math.abs(Math.sin(block.getTicksLived() / 10f)));
            } else {
                vector.setY(0);
            }

            moveBlock(block, vector);
        }
    }

    @Override
    public boolean onBlockInteract(final @NotNull PlayerInteractEvent event, final int level, final EquipmentSlot slot) {
        final Player player = event.getPlayer();
        final ItemStack hand = player.getInventory().getItem(slot);

        if (event.getAction() == RIGHT_CLICK_AIR || event.getAction() == RIGHT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                if (!VORTEX.contains(player)) {
                    VORTEX.add(player);
                }

                int counter = 0;
                for (final Entity idleBlockPlayer : IDLE_BLOCKS.values()) {
                    if (idleBlockPlayer.equals(player)) {
                        counter++;
                    }
                }

                for (Material mat : Material.values()) {
                    if (counter < 64 && mat.isBlock() && player.getInventory().contains(mat)) {
                        Utilities.removeMaterialsFromPlayer(player, mat, 1);
                        Utilities.damageItemStackRespectUnbreaking(player, 2, slot);

                        final Location location = player.getLocation();
                        final Entity blockEntity = spawnBlock(location, mat);
                        blockEntity.setMetadata("ze.anthrothrower", new FixedMetadataValue(ZenchantmentsPlugin.getInstance(), player));
                        IDLE_BLOCKS.put(blockEntity, player);
                        return true;
                    }
                }
            }

            return false;
        } else if ((event.getAction() == LEFT_CLICK_AIR || event.getAction() == LEFT_CLICK_BLOCK) || hand.getType() == AIR) {
            VORTEX.remove(player);

            final List<Entity> toRemove = new ArrayList<>();

            for (final Entity block : IDLE_BLOCKS.keySet()) {
                if (IDLE_BLOCKS.get(block).equals(player)) {
                    ATTACK_BLOCKS.put(block, new Pair<>(this.getPower(), player.getLocation().getDirection()));
                    toRemove.add(block);
                    moveBlock(block,
                        player.getTargetBlock(null, 7)
                            .getLocation()
                            .subtract(player.getLocation())
                            .toVector()
                            .multiply(.25)
                    );
                }
            }

            for (final Entity block : toRemove) {
                IDLE_BLOCKS.remove(block);
                block.setGlowing(true);
            }
        }

        return false;
    }

    private static Entity spawnBlock(final Location location, final Material material) {
        final World world = requireNonNull(location.getWorld());
        final org.bukkit.block.data.BlockData blockData = ZenchantmentsPlugin.getInstance().getServer().createBlockData(material);
        try {
            final Object entity = world.getClass()
                .getMethod("spawnEntity", Location.class, EntityType.class)
                .invoke(world, location, EntityType.BLOCK_DISPLAY);
            final BlockDisplay display = (BlockDisplay) entity;
            display.setBlock(blockData);
            display.setPersistent(false);
            display.setViewRange(32.0f);
            display.setInterpolationDuration(2);
            display.setTeleportDuration(1);
            return display;
        } catch (ReflectiveOperationException | ClassCastException | IllegalArgumentException ignored) {
            final FallingBlock fallingBlock = world.spawnFallingBlock(location, blockData);
            fallingBlock.setDropItem(false);
            fallingBlock.setGravity(false);
            return fallingBlock;
        }
    }

    private static void moveBlock(final Entity block, final Vector movement) {
        if (block instanceof BlockDisplay) {
            block.teleport(block.getLocation().add(movement));
        } else {
            block.setVelocity(movement);
        }
    }

    private static boolean isTooFarFromOwner(final Entity block) {
        if (!block.hasMetadata("ze.anthrothrower")) {
            return true;
        }

        Object owner = block.getMetadata("ze.anthrothrower").get(0).value();
        return !(owner instanceof Player player)
            || !player.isOnline()
            || !Objects.equals(player.getWorld(), block.getWorld())
            || player.getLocation().distanceSquared(block.getLocation()) > 1024;
    }

    private record Pair<K, V>(K key, V value) {

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
