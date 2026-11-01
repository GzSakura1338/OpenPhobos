package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.rotation.RotationManager;
import org.phobos.module.PlacementModule;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.module.player.BuilderModule;
import org.phobos.module.player.BuilderModule;

public final class BuilderPlayerTickListener extends ModuleListener.Safe<BuilderModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(BuilderModule.Mode.Wither) && this.module.nametag.getValue()) {
         if (!this.module.nametagRotation.testObject2(PlacementModule.Rotation.Normal) || !RotationManager.isFlag()) {
            for (Entity entity : clientWorld.getOtherEntities(null, BlockPosUtils.createBox(clientPlayerEntity, this.module.nametagRange.getValue()))) {
               if (!entity.hasCustomName()) {
                  MutableBoolean mutableBoolean = new MutableBoolean();
                  this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                     if (itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.NAME_TAG, 1) != null) {
                        float[] floats = RotationUtils.values(clientPlayerEntity, entity);
                        MutableBoolean mutableBoolean2 = new MutableBoolean();
                        switch ((PlacementModule.Rotation)this.module.nametagRotation.getValue()) {
                           case Instant:
                              this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                              mutableBoolean.setTrue();
                              break;
                           case Normal:
                              PlayerActionUtils.handleClientPlayerEntity17(clientPlayerEntity, floats[0], floats[1], clientPlayerEntity.isOnGround());
                              break;
                           case Hybrid:
                           case Silent:
                              this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                              mutableBoolean2.setTrue();
                        }

                        this.phobosClient.getInteractionManager().handleEntity2(entity, clientPlayerEntity, Hand.MAIN_HAND);
                        if (mutableBoolean2.getValue()) {
                           this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                        }
                     }
                  });
                  if (mutableBoolean.getValue()) {
                     break;
                  }
               }
            }
         }
      }
   }

   public BuilderPlayerTickListener(BuilderModule builderModule) {
      super(builderModule, PlayerTickEvent.class);
   }
}
