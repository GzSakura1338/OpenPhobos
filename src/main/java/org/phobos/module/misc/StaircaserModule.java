package org.phobos.module.misc;

import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;

public class StaircaserModule extends PlacementModule {
   public StaircaserModule(PhobosClient phobosClient) {
      super(phobosClient, "Staircaser", "Makes a staircase", Categories.misc, -4);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = itemSwitcher.resolveObject(
         slot -> slot.getStack().getItem() instanceof BlockItem blockItem && BlockUtils.testItem(blockItem.asItem()) ? blockItem.getBlock() : null
      );
      if (block != null) {
         BlockPos blockPos = BlockPosUtils.createBlockPos2(clientPlayerEntity.getPos(), 1);
         if (BlockUtils.testBlockPos7(blockPos, clientWorld)) {
            BlockPos blockPos2 = (clientPlayerEntity.getPitch() < 0.0F ? blockPos.up() : blockPos.down()).offset(clientPlayerEntity.getHorizontalFacing());
            this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos2);
         }
      }
   }
}
