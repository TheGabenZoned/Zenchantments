package zedly.zenchantments.enchantments;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Canvas extends Zenchantment implements Listener {

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public boolean onBlockInteract(@NotNull PlayerInteractEvent event, int level, final EquipmentSlot slot) {
        if (slot == EquipmentSlot.HAND && !event.getPlayer().isSneaking()
            && event.getAction() == RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) return false;

            Material blockMaterial = block.getType();
            String blockType = WorldInteractionUtil.isDyeableBlock(blockMaterial);
            if (blockType == null) return false;

            final Player player = event.getPlayer();
            ItemStack dyeItem = null;
            int c = -1;

            // Find a suitable dye in hotbar.
            for (int i = 0; i < 9; i++) {
                dyeItem = player.getInventory().getItem(i);
                if (dyeItem != null
                    && MaterialList.DYES.contains(dyeItem.getType())
                ) {
                    c = i;
                    break;
                }
            }

            if (c == -1) {
                // No suitable dye in inventory.
                return false;
            }

            //Ensure matching new block can be found
            Material newMaterial = Utilities.getMaterialFromDyeAndBase(blockMaterial, dyeItem.getType());
            if (newMaterial == null) return false;
            if (newMaterial == blockMaterial) return false;

            //Replace block
            BlockData blockData = block.getBlockData();
            if (WorldInteractionUtil.placeBlock(block,player,newMaterial,blockData)) {
                String materialType = Utilities.getMaterialBase(blockMaterial);
                if (materialType == null) return false;

                BlockState state = block.getState();
                BlockFace originalFacing = null;
                if (materialType.equalsIgnoreCase("GLAZED_TERRACOTTA")) {
                    if (state.getBlockData() instanceof Directional directional) {
                        originalFacing = directional.getFacing();
                    }
                }
                block.setType(newMaterial, true);
                block.getState().update(true, true);
                if (materialType.equalsIgnoreCase("STAINED_GLASS_PANE")) {
                    for (BlockFace face : BlockFace.values()) {
                        if (face == BlockFace.SELF) continue;
                        if (face != BlockFace.NORTH && face != BlockFace.SOUTH && face != BlockFace.EAST && face != BlockFace.WEST)
                            continue;
                        Block neighbor = block.getRelative(face);
                        if (neighbor.getType() != Material.AIR) {
                            MultipleFacing newBlockData = (MultipleFacing) block.getBlockData();
                            newBlockData.setFace(face, true);
                            block.setBlockData(newBlockData);
                        }
                        neighbor.getState().update(true, true);
                    }
                }
                if (materialType.equalsIgnoreCase("GLAZED_TERRACOTTA") && originalFacing != null) {
                    BlockData newData = block.getBlockData();
                    if (newData instanceof Directional newDirectional) {
                        newDirectional.setFacing(originalFacing);
                        block.setBlockData(newDirectional, true);
                    }
                }
                player.playSound(player.getLocation(), Sound.ITEM_BRUSH_BRUSHING_GENERIC, 1.0F, 1.0F);
                Utilities.damageItemStackRespectUnbreaking(player, 1, slot);
                Utilities.removeMaterialsFromPlayer(player, dyeItem.getType(), 1);
            }


        }
        return false;
    }
}
