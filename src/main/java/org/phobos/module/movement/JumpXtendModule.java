package org.phobos.module.movement;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.TimeGate;
import org.phobos.api.Subscribe;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.JumpEvent3;

public class JumpXtendModule extends PlacementModule {
   public final TimeGate.Normal normal3 = new TimeGate.Normal();

   public JumpXtendModule(PhobosClient phobosClient) {
      super(phobosClient, "JumpXtend", "Parkour anti-fall", Categories.movement, 2);
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.normal3.testLong(750L)) {
         Block block = itemSwitcher.resolveObject(
            slot -> slot.getStack().getItem() instanceof BlockItem blockItem && BlockUtils.testItem(slot.getStack().getItem()) ? blockItem.getBlock() : null
         );
         if (block != null) {
            if (!clientPlayerEntity.isOnGround()) {
               PlayerEntity playerEntity2 = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity2(1, clientPlayerEntity);
               if (!(playerEntity2.getY() > clientPlayerEntity.getY())) {
                  PlayerEntity playerEntity3 = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity2(3, clientPlayerEntity);
                  if (!(playerEntity3.fallDistance <= 0.0F) && !playerEntity3.isOnGround()) {
                     for (int i = 5; i < 10; i++) {
                        PlayerEntity playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity2(i, clientPlayerEntity);
                        if (playerEntity.isOnGround()) {
                           return;
                        }
                     }

                     BlockPos blockPos2 = new BlockPos(
                        (int)Math.floor(playerEntity2.getX()), (int)Math.floor(playerEntity2.getY() - 1.0), (int)Math.floor(playerEntity2.getZ())
                     );
                     if (!this.resolvePlacement8(block, blockPos2).isSuccessful()) {
                        List<BlockPos> list = this.resolveList2(blockPos2, clientWorld, clientPlayerEntity, block);
                        if (!list.isEmpty() && list.size() <= 3) {
                           for (BlockPos blockPos : list) {
                              this.resolvePlacement8(block, blockPos);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void handleJumpEvent3(JumpEvent3 jumpEvent3) {
      this.normal3.run();
   }
}
