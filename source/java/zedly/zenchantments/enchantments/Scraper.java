package zedly.zenchantments.enchantments;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.*;

import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

@AZenchantment(runInSlots = Slots.MAIN_HAND, conflicting = {})
public final class Scraper extends Zenchantment implements Listener {

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public boolean onBlockInteract(@NotNull PlayerInteractEvent event, int level, final EquipmentSlot slot) {
        if (slot == EquipmentSlot.HAND && !event.getPlayer().isSneaking()
            && event.getAction() == RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) return false;

            Material blockMaterial = block.getType();
            if (!MaterialList.COPPER_BLOCKS.contains(blockMaterial)) return false;
            if (MaterialList.UNOXIDIZED_COPPER_BLOCKS.contains(blockMaterial)) return false;

            //Ensure matching new block can be found
            Material newMaterial = Utilities.getPreviousOxidationLevel(blockMaterial);
            if (newMaterial == null) return false;
            if (newMaterial == blockMaterial) return false;

            //Replace block
            Player player = event.getPlayer();
            BlockData blockData = block.getBlockData();
            if (WorldInteractionUtil.placeBlock(block,player,newMaterial,blockData)) {
                WorldInteractionUtil.replaceBlockPreservingOrientation(block, newMaterial);
                player.playSound(player.getLocation(), Sound.ITEM_BRUSH_BRUSHING_GENERIC, 1.0F, 1.0F);
                Utilities.damageItemStackRespectUnbreaking(player, 1, slot);
            }


        }
        return false;
    }
}
