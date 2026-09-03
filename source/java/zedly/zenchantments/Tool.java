package zedly.zenchantments;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;
import static org.bukkit.Material.*;

public enum Tool {
    AXE(
        "Axe",
        new Material[] { WOODEN_AXE, STONE_AXE, GOLDEN_AXE, IRON_AXE, DIAMOND_AXE, NETHERITE_AXE, COPPER_AXE }
    ),
    SHOVEL(
        "Shovel",
        new Material[] { WOODEN_SHOVEL, STONE_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL, COPPER_SHOVEL }
    ),
    SWORD(
        "Sword",
        new Material[] { WOODEN_SWORD, STONE_SWORD, GOLDEN_SWORD, IRON_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, COPPER_SWORD, MACE, WOODEN_SPEAR, STONE_SPEAR, COPPER_SPEAR, GOLDEN_SPEAR, IRON_SPEAR, DIAMOND_SPEAR, NETHERITE_SPEAR }
    ),
    SPEAR(
        "Spear",
        new Material[] { WOODEN_SPEAR, STONE_SPEAR, COPPER_SPEAR, GOLDEN_SPEAR, IRON_SPEAR, DIAMOND_SPEAR, NETHERITE_SPEAR }
    ),
    PICKAXE(
        "Pickaxe",
        new Material[] { WOODEN_PICKAXE, STONE_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE, COPPER_PICKAXE }
    ),
    ROD(
        "Rod",
        new Material[] { FISHING_ROD }
    ),
    SHEAR(
        "Shears",
        new Material[] { SHEARS }
    ),
    BOW(
        "Bow",
        new Material[] { Material.BOW, CROSSBOW }
    ),
    TRIDENT(
        "Trident",
        new Material[] {Material.TRIDENT}
    ),
    LIGHTER(
        "Lighter",
        new Material[] { FLINT_AND_STEEL }
    ),
    BRUSH(
        "Brush",
        new Material[] { Material.BRUSH }
    ),
    SHIELD(
        "Shield",
        new Material[] { Material.SHIELD }
    ),
    HOE(
        "Hoe",
        new Material[] { WOODEN_HOE, STONE_HOE, GOLDEN_HOE, IRON_HOE, DIAMOND_HOE, NETHERITE_HOE, COPPER_HOE }
    ),
    HELMET(
        "Helmet",
        new Material[] { NETHERITE_HELMET, DIAMOND_HELMET, IRON_HELMET, GOLDEN_HELMET, CHAINMAIL_HELMET, LEATHER_HELMET, TURTLE_HELMET, COPPER_HELMET }
    ),
    CHESTPLATE(
        "Chestplate",
        new Material[] {
            NETHERITE_CHESTPLATE,
            DIAMOND_CHESTPLATE,
            IRON_CHESTPLATE,
            GOLDEN_CHESTPLATE,
            CHAINMAIL_CHESTPLATE,
            LEATHER_CHESTPLATE,
            COPPER_CHESTPLATE,
            ELYTRA
        }
    ),
    LEGGINGS(
        "Leggings",
        new Material[] {
            NETHERITE_LEGGINGS,
            DIAMOND_LEGGINGS,
            IRON_LEGGINGS,
            GOLDEN_LEGGINGS,
            CHAINMAIL_LEGGINGS,
            LEATHER_LEGGINGS,
            COPPER_LEGGINGS
        }
    ),
    BOOTS(
        "Boots",
        new Material[] { NETHERITE_BOOTS, DIAMOND_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, CHAINMAIL_BOOTS, LEATHER_BOOTS, COPPER_BOOTS }
    ),
    WINGS(
        "Elytra",
        new Material[] { ELYTRA }
    ),
    ALL(
        "All",
        new Material[] {
            WOODEN_AXE,
            STONE_AXE,
            GOLDEN_AXE,
            IRON_AXE,
            DIAMOND_AXE,
            NETHERITE_AXE,
            COPPER_AXE,
            WOODEN_SHOVEL,
            STONE_SHOVEL,
            GOLDEN_SHOVEL,
            IRON_SHOVEL,
            DIAMOND_SHOVEL,
            NETHERITE_SHOVEL,
            COPPER_SHOVEL,
            WOODEN_SWORD,
            STONE_SWORD,
            GOLDEN_SWORD,
            IRON_SWORD,
            DIAMOND_SWORD,
            NETHERITE_SWORD,
            COPPER_SWORD,
            WOODEN_PICKAXE,
            STONE_PICKAXE,
            GOLDEN_PICKAXE,
            IRON_PICKAXE,
            DIAMOND_PICKAXE,
            NETHERITE_PICKAXE,
            COPPER_PICKAXE,
            FISHING_ROD,
            SHEARS,
            Material.BOW,
            CROSSBOW,
            FLINT_AND_STEEL,
            ELYTRA,
            WOODEN_HOE,
            STONE_HOE,
            GOLDEN_HOE,
            IRON_HOE,
            DIAMOND_HOE,
            NETHERITE_HOE,
            COPPER_HOE,
            NETHERITE_HELMET,
            DIAMOND_HELMET,
            IRON_HELMET,
            GOLDEN_HELMET,
            CHAINMAIL_HELMET,
            LEATHER_HELMET,
            COPPER_HELMET,
            NETHERITE_CHESTPLATE,
            DIAMOND_CHESTPLATE,
            IRON_CHESTPLATE,
            GOLDEN_CHESTPLATE,
            CHAINMAIL_CHESTPLATE,
            LEATHER_CHESTPLATE,
            COPPER_CHESTPLATE,
            NETHERITE_LEGGINGS,
            DIAMOND_LEGGINGS,
            IRON_LEGGINGS,
            GOLDEN_LEGGINGS,
            CHAINMAIL_LEGGINGS,
            LEATHER_LEGGINGS,
            COPPER_LEGGINGS,
            NETHERITE_BOOTS,
            DIAMOND_BOOTS,
            IRON_BOOTS,
            GOLDEN_BOOTS,
            CHAINMAIL_BOOTS,
            LEATHER_BOOTS,
            COPPER_BOOTS,
            Material.TRIDENT,
            Material.BRUSH,
            MACE,
            Material.SHIELD,
            WOODEN_SPEAR,
            STONE_SPEAR,
            COPPER_SPEAR,
            GOLDEN_SPEAR,
            IRON_SPEAR,
            DIAMOND_SPEAR,
            NETHERITE_SPEAR
        }
    );

    private final String     id;
    private final Material[] materials;

    Tool(final @NotNull String id, final @NotNull Material[] materials) {
        this.id = id;
        this.materials = materials;
    }

    @Nullable
    public static Tool fromString(final @NotNull String text) {
        for (Tool tool : values()) {
            if (text.equalsIgnoreCase(tool.id)) {
                return tool;
            }
        }

        return null;
    }

    @Nullable
    public static Tool fromMaterial(final @NotNull Material material) {
        requireNonNull(material);

        for (Tool tool : values()) {
            if (tool.contains(material)) {
                return tool;
            }
        }

        return null;
    }

    public static Tool fromItemStack(final @Nullable ItemStack stack) {
        return stack == null ? null : fromMaterial(stack.getType());
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public Material[] getMaterials() {
        return this.materials;
    }

    public boolean contains(final @NotNull Material material) {
        requireNonNull(material);

        return ArrayUtils.contains(this.materials, material);
    }
}
