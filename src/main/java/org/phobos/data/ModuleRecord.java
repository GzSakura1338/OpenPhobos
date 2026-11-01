package org.phobos.data;

import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.combat.CrystalPlacement;
import org.phobos.core.PhobosClient;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.player.SilentHotbarModule;

public record ModuleRecord(PlacesCrystalsModule module, PhobosClient phobos) {

   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      BlockPos blockPos,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent,
      boolean flag
   ) {
      this.handleItemSwitcher2(itemSwitcher, clientPlayerEntity, clientWorld, blockPos, switchMode, silent, flag);
   }

   public void handleItemSwitcher2(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      BlockPos blockPos,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent,
      boolean flag
   ) {
      if (this.module.getBooleanValueSetting27().getValue()
         || !PlayerActionUtils.testClientPlayerEntity7(clientPlayerEntity)
         || switchMode.equals(InteractionManager.SwitchMode.Off)) {
         if (clientWorld.getWorldBorder().contains(blockPos)) {
            Vec3d vec3d;
            if (this.module.getIntegerSetting7().testObject2(0)) {
               if (clientPlayerEntity.getEyePos().squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)
                  > MathHelper.square(this.module.getDoubleValueSetting8().getValue())) {
                  return;
               }

               vec3d = null;
            } else {
               Vec3d vec3d2 = RaycastUtils.resolveVec3d(clientPlayerEntity, new Box(blockPos), this.module.getIntegerSetting7().getValue());
               if (vec3d2 == null) {
                  return;
               }

               if (clientPlayerEntity.getEyePos().squaredDistanceTo(vec3d2) > MathHelper.square(this.module.getDoubleValueSetting8().getValue())) {
                  return;
               }

               vec3d = vec3d2;
            }

            Hand hand;
            if (itemSwitcher.getSlot3().getStack().isOf(Items.END_CRYSTAL)) {
               hand = Hand.MAIN_HAND;
            } else if (itemSwitcher.getSlot2().getStack().isOf(Items.END_CRYSTAL)) {
               hand = Hand.OFF_HAND;
            } else {
               ItemSwitcher.SwitchResult switchResult = switch (switchMode) {
                  case Off -> null;
                  case Normal -> {
                     if (this.module.getBooleanValueSetting32().getValue()) {
                        Slot slot = itemSwitcher.resolveObject(slotArg -> slotArg.getStack().isOf(Items.END_CRYSTAL) ? slotArg : null);
                        if (slot == null) {
                           yield null;
                        } else {
                           SilentHotbarModule.getSilentHotbarManager()
                              .handleObject3(
                                 this.module, slot, 4, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                              );
                           yield ItemSwitcher.SwitchResult.resolveSwitchResult(slot);
                        }
                     } else {
                        yield itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.END_CRYSTAL, 2);
                     }
                  }
                  case Silent -> itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.END_CRYSTAL, silent.getFlag() | 1);
               };
               if (switchResult == null) {
                  return;
               }

               hand = switchResult.hand();
            }

            BlockPos blockPos2 = blockPos.toImmutable();
            this.module.resolvePlacesCrystalsModule(blockPos2.up());
            this.module.getBlockPosMap().put(blockPos2, System.currentTimeMillis());
            PlayerEntity playerEntity = this.phobos.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
            BlockHitResult blockHitResult5 = RaycastUtils.resolveBlockHitResult3(
               playerEntity,
               clientWorld,
               blockPos2,
               blockHitResult -> this.module.getDirectionResolver().testBlockPos(blockHitResult.getBlockPos(), blockHitResult.getSide(), clientWorld, playerEntity),
               true
            );
            BlockHitResult blockHitResult;
            if (blockHitResult5 != null) {
               blockHitResult = blockHitResult5;
            } else if (this.module.getBooleanValueSetting7().getValue()) {
               if (vec3d != null) {
                  Direction direction = this.module.getDirectionResolver().resolveDirection(blockPos2, clientPlayerEntity, clientWorld);
                  if (direction == null) {
                     return;
                  }

                  blockHitResult = new BlockHitResult(vec3d, direction, blockPos2, false);
               } else {
                  Direction direction2 = this.module.getDirectionResolver().resolveDirection(blockPos2, clientPlayerEntity, clientWorld);
                  if (direction2 == null) {
                     return;
                  }

                  Vec3d vec3d3 = new Vec3d(
                     blockPos.getX() + 0.5 - direction2.getOffsetX() * 0.5 + direction2.getOffsetX(),
                     blockPos.getY() + 0.5 - direction2.getOffsetY() * 0.5 + direction2.getOffsetY(),
                     blockPos.getZ() + 0.5 - direction2.getOffsetZ() * 0.5 + direction2.getOffsetZ()
                  );
                  blockHitResult = new BlockHitResult(vec3d3, direction2, blockPos2, false);
               }
            } else {
               blockHitResult = new BlockHitResult(new Vec3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5), Direction.DOWN, blockPos2, false);
            }

            MutableBoolean mutableBoolean = new MutableBoolean(false);
            if (this.module.getBooleanValueSetting17().getValue() && flag) {
               float[] floats = RotationUtils.values2(this.phobos.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity), blockHitResult.getPos());
               switch ((PlacesCrystalsModule.Rotate)this.module.getRotateSetting().getValue()) {
                  case Normal:
                     this.module.getPhobosClient().getRotationManager().testFloat(floats[0], floats[1]);
                     break;
                  case Hybrid:
                     if (!this.module.getPhobosClient().getRotationManager().testFloat(floats[0], floats[1])
                        && (
                           this.module.getIntegerSetting6().getValue().equals(-1)
                              || this.module.getAtomicInteger2().get() <= this.module.getIntegerSetting6().getValue()
                        )) {
                        this.module.getPhobosClient().getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                        this.module.getAtomicInteger2().incrementAndGet();
                        mutableBoolean.setTrue();
                     }
                     break;
                  case Silent:
                     if (this.module.getIntegerSetting6().testObject3(-1)
                        || this.module.getAtomicInteger2().get() <= this.module.getIntegerSetting6().getValue()) {
                        this.module.getPhobosClient().getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                        this.module.getAtomicInteger2().incrementAndGet();
                        mutableBoolean.setTrue();
                     }
               }
            }

            this.module.handleClientPlayerEntity6(clientPlayerEntity, clientWorld, blockHitResult, hand);
            if (this.module.getBooleanValueSetting17().getValue() && this.module.getRotateSetting().getValue().reset() && mutableBoolean.getValue()) {
               this.module.getPhobosClient().getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
            }

            this.module.handleBlockPos3(blockPos2);
            this.module.getThreaded3().run();
            if (this.module.getBooleanValueSetting28().getValue()) {
               double d = blockPos.getX() + 0.5;
               double d2 = blockPos.getY() + 1;
               double d3 = blockPos.getZ() + 0.5;
               EndCrystalEntity endCrystalEntity = new EndCrystalEntity(clientWorld, d, d2, d3);
               EntityUtils.handleEntity(endCrystalEntity, d, d2, d3);
               this.module.getCrystalQueue().handleEndCrystalEntity(endCrystalEntity);
            }
         }
      }
   }

   public void handleItemSwitcher3(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      CrystalPlacement crystalPlacement,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent
   ) {
      if (this.module.getBooleanValueSetting27().getValue()
         || !PlayerActionUtils.testClientPlayerEntity7(clientPlayerEntity)
         || switchMode.equals(InteractionManager.SwitchMode.Off)) {
         Hand hand;
         if (itemSwitcher.getSlot3().getStack().isOf(Items.END_CRYSTAL)) {
            hand = Hand.MAIN_HAND;
         } else if (itemSwitcher.getSlot2().getStack().isOf(Items.END_CRYSTAL)) {
            hand = Hand.OFF_HAND;
         } else {
            ItemSwitcher.SwitchResult switchResult = switch (switchMode) {
               case Off -> null;
               case Normal -> {
                  if (this.module.getBooleanValueSetting32().getValue()) {
                     Slot slot = itemSwitcher.resolveObject(slotArg -> slotArg.getStack().isOf(Items.END_CRYSTAL) ? slotArg : null);
                     if (slot == null) {
                        yield null;
                     } else {
                        SilentHotbarModule.getSilentHotbarManager()
                           .handleObject3(
                              this.module, slot, 4, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                           );
                        yield ItemSwitcher.SwitchResult.resolveSwitchResult(slot);
                     }
                  } else {
                     yield itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.END_CRYSTAL, 2);
                  }
               }
               case Silent -> itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.END_CRYSTAL, silent.getFlag() | 1);
            };
            if (switchResult == null) {
               return;
            }

            hand = switchResult.hand();
         }

         BlockPos blockPos = crystalPlacement.toImmutable();
         this.module.resolvePlacesCrystalsModule(blockPos.up());
         this.module.getBlockPosMap().put(blockPos, System.currentTimeMillis());
         PlayerEntity playerEntity2 = this.phobos.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
         BlockHitResult blockHitResult4 = RaycastUtils.resolveBlockHitResult3(
            playerEntity2,
            clientWorld,
            blockPos,
            blockHitResult -> this.module.getDirectionResolver().testBlockPos(blockHitResult.getBlockPos(), blockHitResult.getSide(), clientWorld, playerEntity2),
            true
         );
         BlockHitResult blockHitResult;
         if (blockHitResult4 != null) {
            blockHitResult = blockHitResult4;
         } else if (this.module.getBooleanValueSetting7().getValue()) {
            Direction direction = this.module.getDirectionResolver().resolveDirection(blockPos, clientPlayerEntity, clientWorld);
            if (direction == null) {
               return;
            }

            Vec3d vec3d = new Vec3d(
               crystalPlacement.getX() + 0.5 - direction.getOffsetX() * 0.5 + direction.getOffsetX(),
               crystalPlacement.getY() + 0.5 - direction.getOffsetY() * 0.5 + direction.getOffsetY(),
               crystalPlacement.getZ() + 0.5 - direction.getOffsetZ() * 0.5 + direction.getOffsetZ()
            );
            blockHitResult = new BlockHitResult(vec3d, direction, blockPos, false);
         } else {
            blockHitResult = new BlockHitResult(
               new Vec3d(crystalPlacement.getX() + 0.5, crystalPlacement.getY(), crystalPlacement.getZ() + 0.5), Direction.DOWN, blockPos, false
            );
         }

         MutableBoolean mutableBoolean = new MutableBoolean(false);
         if (this.module.getBooleanValueSetting17().getValue()) {
            float[] floats = RotationUtils.values2(playerEntity2, blockHitResult.getPos());
            switch ((PlacesCrystalsModule.Rotate)this.module.getRotateSetting().getValue()) {
               case Hybrid:
               case Silent:
                  if (this.module.getIntegerSetting6().testObject3(-1) || this.module.getAtomicInteger2().get() <= this.module.getIntegerSetting6().getValue()) {
                     this.module.getPhobosClient().getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                     this.module.getAtomicInteger2().incrementAndGet();
                     mutableBoolean.setTrue();
                  }
            }
         }

         if (this.module.getBooleanValueSetting14().getValue() && this.module.getRotateSetting().getValue().equals(PlacesCrystalsModule.Rotate.Normal)) {
            PlayerEntity playerEntity = this.phobos.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
            if (!RaycastUtils.testEntity2(playerEntity, clientWorld, blockHitResult.getBlockPos(), blockHitResult.getSide())) {
               this.module.handleBlockPos3(blockPos);
               this.module.getThreaded3().run();
               return;
            }
         }

         this.module.handleClientPlayerEntity6(clientPlayerEntity, clientWorld, blockHitResult, hand);
         if (this.module.getBooleanValueSetting17().getValue() && this.module.getRotateSetting().getValue().reset() && mutableBoolean.getValue()) {
            this.module.getPhobosClient().getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
         }

         this.module.handleBlockPos3(blockPos);
         this.module.getThreaded3().run();
         if (this.module.getBooleanValueSetting28().getValue()) {
            double d = crystalPlacement.getX() + 0.5;
            double d2 = crystalPlacement.getY() + 1;
            double d3 = crystalPlacement.getZ() + 0.5;
            EndCrystalEntity endCrystalEntity = new EndCrystalEntity(clientWorld, d, d2, d3);
            EntityUtils.handleEntity(endCrystalEntity, d, d2, d3);
            this.module.getCrystalQueue().handleEndCrystalEntity(endCrystalEntity);
         }
      }
   }

   public void handleItemSwitcher4(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      CrystalPlacement crystalPlacement,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent
   ) {
      if (crystalPlacement.isFlag32()) {
         if (itemSwitcher.testItem2(Blocks.OBSIDIAN.asItem())) {
            List<BlockPos> list = crystalPlacement.getBlockPosList();
            if (list == null) {
               return;
            }

            for (BlockPos blockPos : list) {
               this.module.resolvePlacement8(Blocks.OBSIDIAN, blockPos);
            }
         }
      } else {
         this.handleItemSwitcher3(itemSwitcher, clientPlayerEntity, clientWorld, crystalPlacement, switchMode, silent);
      }
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      BlockPos blockPos,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent
   ) {
      this.phobos
         .getInventoryManager()
         .handleConsumer2(itemSwitcher -> this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, blockPos, switchMode, silent, true));
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, CrystalPlacement crystalPlacement) {
      this.phobos
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> this.handleItemSwitcher4(
               itemSwitcher,
               clientPlayerEntity,
               clientWorld,
               crystalPlacement,
               this.module.getSwitchModeSetting2().getValue(),
               this.module.getSilentSetting3().getValue()
            )
         );
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      BlockPos blockPos,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent
   ) {
      this.phobos
         .getInventoryManager()
         .handleConsumer2(itemSwitcher -> this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, blockPos, switchMode, silent, false));
   }
}
