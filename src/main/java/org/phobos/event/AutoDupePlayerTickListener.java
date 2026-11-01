package org.phobos.event;

import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.AreaScanner;
import org.phobos.api.KeyValue;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.module.misc.AutoDupeModule;
import org.phobos.module.misc.AutoDupeModule;

public final class AutoDupePlayerTickListener extends ModuleListener.Safe<AutoDupeModule, PlayerTickEvent> {
   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ItemSwitcher itemSwitcher,
      ItemFrameEntity itemFrameEntity,
      Set<Item> set
   ) {
      if (this.module.normal.testNumber(this.module.longValueSetting.getValue())) {
         if (this.module.booleanValueSetting4.getValue()) {
            float[] floats = RotationUtils.values(clientPlayerEntity, itemFrameEntity);
            this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
         }

         ItemStack itemStack = itemFrameEntity.getHeldItemStack();
         Slot slot = itemSwitcher.resolveObject(slotx -> set.contains(slotx.getStack().getItem()) ? slotx : null);
         if (itemStack.isEmpty()) {
            if (slot != null) {
               itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1);
               this.module.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, itemFrameEntity, false);
            }
         } else {
            int i3 = this.module.integerSetting.getValue();
            if (i3 != 0) {
               int i = itemFrameEntity.getId();
               if (!this.module.integerMap.containsKey(i)) {
                  this.module.integerMap.put(i, i3);
                  return;
               }

               int i2 = this.module.integerMap.get(i);
               if (i2 > 0) {
                  if (slot == null) {
                     return;
                  }

                  itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1);
                  this.module.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, itemFrameEntity, false);
                  this.module.integerMap.put(i, i2 - 1);
                  return;
               }

               this.module.integerMap.remove(i);
            }

            this.module
               .handleClientPlayerEntity(
                  clientPlayerEntity, clientPlayerInteractionManager, itemFrameEntity, true, itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1) != null
               );
         }
      }
   }

   public AutoDupePlayerTickListener(AutoDupeModule autoDupeModule) {
      super(autoDupeModule, PlayerTickEvent.class);
   }

   public KeyValue<Boolean, BlockPos> resolveKeyValue(ClientWorld clientWorld, BlockPos blockPos) {
      for (Direction direction : DirectionUtils.directionArray) {
         BlockPos blockPos2 = blockPos.offset(direction);
         BlockState blockState = clientWorld.getBlockState(blockPos2);
         if (blockState.isOf(Blocks.LEVER)) {
            return KeyValue.createValue(true, blockPos2);
         }
      }

      return KeyValue.createValue(false, null);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoDupeModule.Mode)this.module.modeSetting.getValue()) {
         case Frame:
            if (!this.module.booleanValueSetting4.getValue() || !RotationManager.isFlag()) {
               Set<Item> set = this.module.setSetting.getValue();
               if (!set.isEmpty()) {
                  this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                     if (this.module.booleanValueSetting2.getValue()) {
                        for (Entity entity : clientWorld.getEntities()) {
                           if (entity instanceof ItemFrameEntity itemFrameEntity && !itemFrameEntity.fixed) {
                              double d = clientPlayerEntity.getEyePos().distanceTo(itemFrameEntity.getPos());
                              if (!(d > this.module.doubleValueSetting.getValue())) {
                                 this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, itemSwitcher, itemFrameEntity, set);
                              }
                           }
                        }
                     } else {
                        MutableDouble mutableDouble = new MutableDouble(Double.MAX_VALUE);
                        MutableObject<ItemFrameEntity> mutableObject = new MutableObject();

                        for (Entity entity2 : clientWorld.getEntities()) {
                           if (entity2 instanceof ItemFrameEntity itemFrameEntity2 && !itemFrameEntity2.fixed) {
                              double d2 = clientPlayerEntity.getEyePos().distanceTo(itemFrameEntity2.getPos());
                              if (!(d2 > this.module.doubleValueSetting.getValue()) && d2 < mutableDouble.getValue()) {
                                 mutableObject.setValue(itemFrameEntity2);
                                 mutableDouble.setValue(d2);
                              }
                           }
                        }

                        ItemFrameEntity itemFrameEntity3 = (ItemFrameEntity)mutableObject.getValue();
                        if (itemFrameEntity3 != null) {
                           this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, itemSwitcher, itemFrameEntity3, set);
                        }
                     }
                  });
               }
            }
            break;
         case Piston:
            Set<Item> set2 = this.module.setSetting.getValue();
            if (!set2.isEmpty()) {
               this.phobosClient
                  .getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        AreaScanner.Area area = new AreaScanner.Area();
                        area.handleVec3d(clientPlayerEntity.getEyePos(), this.module.doubleValueSetting.getValue(), blockPosx -> true);

                        for (BlockPos blockPos3 : area) {
                           BlockState blockState = clientWorld.getBlockState(blockPos3);
                           if (blockState.getBlock() instanceof PistonBlock) {
                              KeyValue<Boolean, BlockPos> keyValue = this.resolveKeyValue(clientWorld, blockPos3);
                              if (keyValue.getObject()) {
                                 boolean flag = (Boolean)blockState.get(PistonBlock.EXTENDED);
                                 Direction direction = (Direction)blockState.get(FacingBlock.FACING);
                                 BlockPos blockPos2 = blockPos3.offset(direction.getOpposite());

                                 for (Entity entity : clientWorld.getOtherEntities(null, BoxUtils.createBox9(blockPos2))) {
                                    if (entity instanceof ItemFrameEntity itemFrameEntity) {
                                       BlockPos blockPos = itemFrameEntity.getAttachedBlockPos().offset(itemFrameEntity.getHorizontalFacing().getOpposite());
                                       if (blockPos.equals(blockPos3)) {
                                          ItemStack itemStack = itemFrameEntity.getHeldItemStack();
                                          if (itemStack.isEmpty()) {
                                             Slot slot = itemSwitcher.resolveObject(slotx -> set2.contains(slotx.getStack().getItem()) ? slotx : null);
                                             if (slot == null) {
                                                return;
                                             }

                                             itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1);
                                             clientPlayerInteractionManager.interactEntity(clientPlayerEntity, itemFrameEntity, Hand.MAIN_HAND);
                                             this.module.handleEntity(itemFrameEntity);
                                             return;
                                          }

                                          Runnable runnable = () -> {
                                             BlockPos blockPosx = keyValue.getObject2();
                                             this.phobosClient
                                                .getInteractionManager()
                                                .handleBlockPos2(
                                                   blockPosx,
                                                   clientPlayerEntity,
                                                   clientWorld,
                                                   ExactDirectionResolver.exactDirectionResolver,
                                                   this.phobosClient.getInteractionManager().getConsumer()
                                                );
                                             this.module.handleClientWorld(clientWorld, blockPosx, clientWorld.getBlockState(blockPosx));
                                          };
                                          if (flag) {
                                             clientPlayerInteractionManager.attackEntity(clientPlayerEntity, itemFrameEntity);
                                             this.module.handleEntity(itemFrameEntity);
                                             runnable.run();
                                             return;
                                          }

                                          runnable.run();
                                          return;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  );
            }
         case SalC1:
      }
   }
}
