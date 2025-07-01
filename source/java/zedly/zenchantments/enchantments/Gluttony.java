package zedly.zenchantments.enchantments;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

import java.util.*;

import static org.bukkit.Material.*;

@AZenchantment(runInSlots = Slots.ARMOR, conflicting = {})
public final class Gluttony extends Zenchantment {
    private static final HashMap<Material, String> GLUTTONY_FOODS = new HashMap<>();

    @Override
    public boolean onScan(final @NotNull Player player, final int level, final EquipmentSlot slot) {
        final int needFoodLevel = 20 - player.getFoodLevel();
        if (needFoodLevel <= 0) {
            return false;
        }
        final double genericMaxHealth = Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue();
        boolean needToHeal = player.getHealth() < genericMaxHealth;

        Material maxHungerMaterial = AIR;
        int maxFoodLevel = 0;
        Material maxNonExcessMaterial = AIR;
        int maxNonExcessFoodLevel = 0;
        for (ItemStack item : player.getInventory()) {
            if (item == null) {
                continue;
            }
            Material mat = item.getType();
            if (!GLUTTONY_FOODS.containsKey(mat)) {
                continue;
            }

            int foodLevelForMat = Integer.parseInt(GLUTTONY_FOODS.get(mat).split("\\|")[0]);
            if (foodLevelForMat > maxFoodLevel) {
                maxHungerMaterial = mat;
                maxFoodLevel = foodLevelForMat;
                if (!needToHeal && needFoodLevel < maxFoodLevel) {
                    return false;
                }
            }
            if (foodLevelForMat <= needFoodLevel && foodLevelForMat > maxNonExcessFoodLevel) {
                maxNonExcessMaterial = mat;
                maxNonExcessFoodLevel = foodLevelForMat;
            }
        }
        Material matToEat = needToHeal ? maxNonExcessMaterial : maxHungerMaterial;
        if(matToEat == AIR) {
            return false;
        }

        final int foodLevel = Integer.parseInt(GLUTTONY_FOODS.get(matToEat).split("\\|")[0]);
        final float saturationlevel = Float.parseFloat(GLUTTONY_FOODS.get(matToEat).split("\\|")[1]);

        Utilities.removeMaterialsFromPlayer(player, matToEat, 1);

        player.setFoodLevel(player.getFoodLevel() + foodLevel);
        player.setSaturation(player.getSaturation() + saturationlevel);

        if (matToEat == RABBIT_STEW
            || matToEat == MUSHROOM_STEW
            || matToEat == BEETROOT_SOUP
        ) {
            player.getInventory().addItem(new ItemStack(BOWL));
        }
        if (matToEat == HONEY_BOTTLE
        ) {
            HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(new ItemStack(GLASS_BOTTLE));
            if (!overflow.isEmpty()) {
                player.getWorld().dropItem(player.getLocation(), overflow.get(0));
            }
        }
        return true;
    }

    static {
        GLUTTONY_FOODS.put(APPLE, "4|2.4");
        GLUTTONY_FOODS.put(BAKED_POTATO, "5|6.0");
        GLUTTONY_FOODS.put(BEETROOT, "1|1.2");
        GLUTTONY_FOODS.put(BEETROOT_SOUP, "6|7.2");
        GLUTTONY_FOODS.put(BREAD, "5|6.0");
        GLUTTONY_FOODS.put(CARROT, "3|3.6");
        GLUTTONY_FOODS.put(COOKED_CHICKEN, "6|7.2");
        GLUTTONY_FOODS.put(COOKED_COD, "5|6.0");
        GLUTTONY_FOODS.put(COOKED_MUTTON, "6|9.6");
        GLUTTONY_FOODS.put(COOKED_PORKCHOP, "8|12.8");
        GLUTTONY_FOODS.put(COOKED_RABBIT, "5|6.0");
        GLUTTONY_FOODS.put(COOKED_SALMON, "6|9.6");
        GLUTTONY_FOODS.put(COOKIE, "2|0.4");
        GLUTTONY_FOODS.put(DRIED_KELP, "1|0.6");
        GLUTTONY_FOODS.put(GLOW_BERRIES, "2|0.4");
        GLUTTONY_FOODS.put(GOLDEN_CARROT, "6|14.4");
        GLUTTONY_FOODS.put(HONEY_BOTTLE, "6|1.2");
        GLUTTONY_FOODS.put(MELON_SLICE, "2|1.2");
        GLUTTONY_FOODS.put(MUSHROOM_STEW, "6|7.2");
        GLUTTONY_FOODS.put(POTATO, "1|0.6");
        GLUTTONY_FOODS.put(PUMPKIN_PIE, "8|4.8");
        GLUTTONY_FOODS.put(RABBIT_STEW, "10|12.0");
        GLUTTONY_FOODS.put(COOKED_BEEF, "8|12.8");
        GLUTTONY_FOODS.put(SWEET_BERRIES, "2|0.4");
        GLUTTONY_FOODS.put(TROPICAL_FISH, "1|0.2");
    }
}
