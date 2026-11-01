package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;

public class AntiDropModule extends PlacementModule {
   public AntiDropModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiDrop", "Prevents falling blocks", Categories.combat, 5);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = itemSwitcher.resolveObject(slot -> {
         if (slot.getStack().getItem() instanceof BlockItem blockItem) {
            Block blockx = blockItem.getBlock();
            return !(blockx instanceof PressurePlateBlock) && !(blockx instanceof ButtonBlock) ? null : blockx;
         } else {
            return null;
         }
      });
      if (block != null) {
         Box box = clientPlayerEntity.getBoundingBox().stretch(0.0, 1.0, 0.0);

         for (Entity entity : clientWorld.getEntities()) {
            if (entity instanceof FallingBlockEntity fallingBlockEntity && box.intersects(fallingBlockEntity.getBoundingBox())) {
               for (BlockPos blockPos : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getY() + 0.25)) {
                  if (BlockUtils.testBlockPos7(blockPos.down(), clientWorld)) {
                     this.resolvePlacement8(block, blockPos);
                  }
               }

               return;
            }
         }
      }
   }
}
