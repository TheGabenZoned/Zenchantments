package zedly.zenchantments;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zedly.zenchantments.event.ZenBlockPlaceEvent;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static org.bukkit.Material.BAMBOO;

public class WorldInteractionUtil {
    private WorldInteractionUtil() {
    }


    public static void collectExp(final @NotNull Player player, final int amount) {
        Location location = player.getLocation();
        player.getWorld().spawn(location, ExperienceOrb.class).setExperience(amount);
    }

    public static boolean breakBlock(final @NotNull Block block, final @NotNull Player player) {
        return player.breakBlock(block);
    }

    public static boolean placeBlock(
        final @NotNull Block blockPlaced,
        final @NotNull Player player,
        final @NotNull Material material,
        final @Nullable BlockData blockData
    ) {
        final Block blockAgainst = blockPlaced.getRelative(blockPlaced.getY() == 0 ? BlockFace.UP : BlockFace.DOWN);
        final ItemStack itemHeld = new ItemStack(material);
        final BlockPlaceEvent placeEvent = new ZenBlockPlaceEvent(
            blockPlaced,
            blockPlaced.getState(),
            blockAgainst,
            itemHeld,
            player,
            true,
            EquipmentSlot.HAND
        );

        Bukkit.getServer().getPluginManager().callEvent(placeEvent);

        if (placeEvent.isCancelled()) {
            return false;
        }

        blockPlaced.setType(material);
        if (blockData != null) {
            blockPlaced.setBlockData(blockData);
        }

        if (MaterialList.LEAVES.contains(material)) {
            final Leaves leaves = (Leaves) blockPlaced.getBlockData();
            leaves.setPersistent(true);
            blockPlaced.setBlockData(leaves);
        }

        return true;
    }

    public static boolean attackEntity(final @NotNull LivingEntity target, final @NotNull Player attacker, final double damage) {
        DamageSource.Builder damageSourceB = DamageSource.builder(DamageType.GENERIC);
        damageSourceB.withCausingEntity(attacker);
        damageSourceB.withDamageLocation(target.getLocation());
        damageSourceB.withDirectEntity(attacker);
        DamageSource damageSource = damageSourceB.build();

        final EntityDamageEvent entityDamageEvent = new EntityDamageEvent(target, DamageCause.ENTITY_ATTACK, damageSource, damage);
        Bukkit.getServer().getPluginManager().callEvent(entityDamageEvent);
        if (damage == 0 || entityDamageEvent.isCancelled()) {
            return !entityDamageEvent.isCancelled();
        }

        target.damage(damage, attacker);
        return true;
    }

    public static boolean canAnimalEnterLoveMode(Animals animal) {
        if (animal.isAdult()) {
            return animal.getLoveModeTicks() == 0 && !animal.isLoveMode();
        }
        return false;
    }

    public static void animalEnterLoveMode(Animals animal, Player feeder) {
        animal.setLoveModeTicks(200);
        animal.setBreedCause(feeder.getUniqueId());
    }

    public static boolean igniteEntity(final @NotNull Entity target, final @NotNull Player player, final int duration) {
        final EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(target, player, duration);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        target.setFireTicks(duration);
        return true;

    }

    public static void damagePlayer(final @NotNull Player player, final double damage, final @NotNull DamageCause cause) {
        DamageSource.Builder damageSourceB = DamageSource.builder(DamageType.GENERIC);
        damageSourceB.withCausingEntity(player);
        damageSourceB.withDamageLocation(player.getLocation());
        damageSourceB.withDirectEntity(player);
        DamageSource damageSource = damageSourceB.build();

        final EntityDamageEvent entityDamageEvent = new EntityDamageEvent(player, cause, damageSource, damage);
        Bukkit.getServer().getPluginManager().callEvent(entityDamageEvent);
        if (damage == 0 || entityDamageEvent.isCancelled()) {
            return;
        }

        player.damage(damage, player);
    }

    public static boolean formBlock(final @NotNull Block block, final @NotNull Material material, final @NotNull Player player) {
        final BlockState state = block.getState();
        state.setType(material);

        final EntityBlockFormEvent event = new EntityBlockFormEvent(player, block, state);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        block.setType(material);

        return true;
    }

    public static Entity showShulker(final @NotNull Block blockToHighlight, final int entityId, final @NotNull Player player) {
        return showHighlightBlock(blockToHighlight, entityId, player);
    }

    public static void hideFakeEntity(World world, int entityId) {
        List<Entity> entityList = world.getEntities();
        for (Entity entity: entityList) {
            if (entity.hasMetadata("temporary")) {
                entity.remove();
                return;
            }
        }
    }

    public static boolean isZombie(final @NotNull Entity entity) {
        return entity.getType() == EntityType.ZOMBIE
            || entity.getType() == EntityType.ZOMBIE_VILLAGER
            || entity.getType() == EntityType.HUSK;
    }

    public static boolean isBlockSafeToBreak(final @NotNull Block block) {
        final Material material = block.getType();
        return material.isSolid()
            && !block.isLiquid()
            && !MaterialList.INTERACTABLE_BLOCKS.contains(material)
            && !MaterialList.UNBREAKABLE_BLOCKS.contains(material)
            && !MaterialList.STORAGE_BLOCKS.contains(material);
    }

    public static boolean grow(@NotNull Block cropBlock, final @NotNull Player player) {
        Material material = cropBlock.getType();

        BlockData data = cropBlock.getBlockData();

        switch (material) {
            case PUMPKIN_STEM:
            case MELON_STEM:
            case CARROTS:
            case WHEAT:
            case POTATOES:
            case COCOA:
            case NETHER_WART:
            case BEETROOTS:
            case SWEET_BERRY_BUSH:
                final BlockData cropState = cropBlock.getBlockData();
                if (cropState instanceof Ageable ageable) {

                    if (ageable.getAge() >= ageable.getMaximumAge()) {
                        return false;
                    }

                    ageable.setAge(ageable.getAge() + 1);
                    data = ageable;
                }
                break;
            case BAMBOO_SAPLING: {
                if (!placeBlock(cropBlock, player, BAMBOO, null)) {
                    return false;
                }

                final Bamboo bamboo = (Bamboo) cropBlock.getBlockData();

                cropBlock = cropBlock.getRelative(BlockFace.UP);

                bamboo.setLeaves(Bamboo.Leaves.SMALL);
                material = BAMBOO;

                data = bamboo;
                break;
            }
            case BAMBOO: {
                final Bamboo bamboo = (Bamboo) cropBlock.getBlockData();

                // Only grow if argument is the base block.
                if (cropBlock.getRelative(BlockFace.DOWN).getType() == material) {
                    return false;
                }

                int height = 1;

                Block testBlock = cropBlock;
                while ((testBlock = testBlock.getRelative(BlockFace.UP)).getType() == material) {
                    // Cancel if cactus/cane is fully grown.
                    if (++height >= 16) {
                        return false;
                    }
                }

                height++;

                boolean result = placeBlock(testBlock, player, material, null);

                if (!result) {
                    return false;
                }

                bamboo.setAge(0);

                if (height == 4) {
                    // Top piece.
                    bamboo.setLeaves(Bamboo.Leaves.LARGE);
                    bamboo.setAge(1);

                    result = placeBlock(cropBlock.getRelative(0, 3, 0), player, material, bamboo);

                    if (!result) {
                        return false;
                    }
                }

                if (height == 3 || height == 4) {
                    // Top piece (height = 3) or second from top (height = 4).
                    bamboo.setLeaves(Bamboo.Leaves.SMALL);
                    bamboo.setAge(height == 4 ? 1 : 0);

                    result = placeBlock(cropBlock.getRelative(0, 2, 0), player, material, bamboo);

                    if (!result) {
                        return false;
                    }

                    // Second from bottom piece.
                    bamboo.setAge(0);
                    bamboo.setLeaves(Bamboo.Leaves.NONE);

                    result = placeBlock(cropBlock, player, material, bamboo);

                    if (!result) {
                        return false;
                    }

                    bamboo.setLeaves(Bamboo.Leaves.SMALL);

                    result = placeBlock(cropBlock.getRelative(0, 1, 0), player, material, bamboo);

                    if (!result) {
                        return false;
                    }

                }

                if (height <= 4) {
                    return true;
                }

                for (int i = height - 1; i >= 0; i--) {
                    final Bamboo.Leaves leaves = i < height - 3 ? Bamboo.Leaves.NONE : i == height - 3
                        ? Bamboo.Leaves.SMALL
                        : Bamboo.Leaves.LARGE;

                    bamboo.setLeaves(leaves);
                    bamboo.setAge(height == 5 && i < 2 ? 0 : 1);

                    result = placeBlock(cropBlock.getRelative(0, i, 0), player, material, bamboo);

                    if (!result) {
                        return false;
                    }
                }

                return true;
            }
            case CACTUS:
            case SUGAR_CANE:
                // Only grow if argument is the base block.
                if (cropBlock.getRelative(BlockFace.DOWN).getType() == material) {
                    return false;
                }

                int height = 1;

                while ((cropBlock = cropBlock.getRelative(BlockFace.UP)).getType() == material) {
                    // Cancel if cactus/cane is fully grown.
                    if (++height >= 3) {
                        return false;
                    }
                }

                // Only grow if argument is the base block.
                if (!MaterialList.AIR.contains(cropBlock.getType())) {
                    return false;
                }

                break;
            default:
                return false;
        }

        return placeBlock(cropBlock, player, material, data);
    }

    public static boolean pickBerries(final @NotNull Block berryBlock, final @NotNull Player player) {
        final BlockData data = berryBlock.getBlockData();
        final Ageable ageable = (Ageable) data;

        // Age of ripe Berries.
        if (ageable.getAge() <= 1) {
            return false;
        }

        final PlayerHarvestBlockEvent event = new PlayerHarvestBlockEvent(
            player,
            berryBlock,
            List.of(new ItemStack(Material.SWEET_BERRIES, 2))
        );

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        // Natural drop rate. Age 2 -> 1-2 berries, Age 3 -> 2-3 berries
        final int numDropped = (ageable.getAge() == 3 ? 2 : 1) + (ThreadLocalRandom.current().nextBoolean() ? 1 : 0);

        // Picked adult berry bush
        ageable.setAge(1);

        berryBlock.setBlockData(ageable);
        berryBlock.getWorld().dropItem(
            berryBlock.getLocation(),
            new ItemStack(Material.SWEET_BERRIES, numDropped)
        );

        return true;
    }

    public static Map<Enchantment, Integer> getPrematureEnchantments(ItemMeta meta) {
        try {
            Field f;
            switch(meta.getClass().getSimpleName()) {
                case "CraftMetaItem":
                case "CraftMetaEnchantedBook":
                    f = meta.getClass().getDeclaredField("enchantments");
                    break;
                case "CraftMetaColorableArmor":
                    f = meta.getClass().getSuperclass().getSuperclass().getDeclaredField("enchantments");
                    break;
                default:
                    f = meta.getClass().getSuperclass().getDeclaredField("enchantments");
                    break;
            }
            f.setAccessible(true);
            Map enchantments = (Map) (f.get(meta));
            return enchantments;
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("ZenchantmentsPlugin")).getLogger().log(Level.SEVERE, "Unable to handle premature ItemMeta " + meta.getClass().getName());
        }
        return null;
    }

    private static Entity showHighlightBlock(final @NotNull Block block, int entityId, final @NotNull Player player) {
        return showHighlightBlock(block.getX(), block.getY(), block.getZ(), entityId, player);
    }

    private static Entity showHighlightBlock(
        final int x,
        final int y,
        final int z,
        final int entityId,
        final @NotNull Player player
    ) {
        try {
            final Entity entity = generateShulkerSpawnPacket(player, x, y, z, entityId);
            entity.setGlowing(true);
            return entity;
        } catch (InstantiationException ex) {
            return null;
        }
    }

    @NotNull
    private static Entity generateShulkerSpawnPacket(
        Player player, final int x, final int y, final int z, final int entityId) throws InstantiationException {
        World world = player.getWorld();
        Location location = new Location(world,x,y,z);
        Entity fel = world.spawnEntity(location, EntityType.SHULKER);
        fel.setVisibleByDefault(false);
        fel.setSilent(true);
        if (fel instanceof LivingEntity) {
            ((LivingEntity) fel).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
            ((LivingEntity) fel).setAI(false);
        }
        fel.setMetadata("temporary",new FixedMetadataValue(ZenchantmentsPlugin.getInstance(),"temporary"));
        player.showEntity(ZenchantmentsPlugin.getInstance(), fel);
        return fel;
    }

    public static void showQuakeBlock(Player player, final Block block) {
        World world = player.getWorld();
        BlockData blockData = block.getBlockData();
        FallingBlock fb = world.spawnFallingBlock(block.getLocation().add(0.5,0,0.5), blockData);
        fb.setMetadata("temporary",new FixedMetadataValue(ZenchantmentsPlugin.getInstance(),"temporary"));
    }

    public static String isDyeableBlock(Material material) {
        if (MaterialList.DYEABLE_CARPETS.contains(material)) return "carpet";
        if (MaterialList.DYEABLE_STAINED_GLASS.contains(material)) return "stained_glass";
        if (MaterialList.STAINED_GLASS_PANES.contains(material)) return "stained_glass_pane";
        if (MaterialList.WOOL.contains(material)) return "wool";
        if (MaterialList.CONCRETE.contains(material)) return "concrete";
        if (MaterialList.CONCRETE_POWDER.contains(material)) return "concrete_powder";
        if (MaterialList.TERRACOTTA.contains(material)) return "terracotta";
        if (MaterialList.GLAZED_TERRACOTTA.contains(material)) return "glazed_terracotta";
        return null;
    }
}
