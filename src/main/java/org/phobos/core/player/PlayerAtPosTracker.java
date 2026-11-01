package org.phobos.core.player;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.combat.AutoMineModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.module.combat.AutoMineModule.Position.Base;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.Feature;
import org.phobos.core.FeatureRegistry;
import org.phobos.core.PhobosClient;

public class PlayerAtPosTracker extends Feature {
   public final PhobosClient phobosClient;

   public boolean testBlockPos(BlockPos blockPos) {
      return this.phobosClient.getBlockBreakManager().getBlockPosMap3().containsKey(blockPos);
   }

   public PlayerAtPosTracker(PhobosClient phobosClient, FeatureRegistry featureRegistry) {
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (featureRegistry.getEngineModule().check5()) {
                  phobosClient.getBlockBreakManager()
                     .getBlockPosMap3()
                     .entrySet()
                     .removeIf(entry -> PlayerAtPosTracker.this.resolvePlayerEntity(entry.getKey(), clientWorld) == null);
                  Set<BlockPos> set2 = phobosClient.getSurroundManager().resolveSet2(clientPlayerEntity, true);
                  Set<BlockPos> set3 = BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY + 0.25);
                  set2.removeIf(blockPos -> blockPos.getY() < clientPlayerEntity.getY() - 0.45);
                  MutableObject<PlayerEntity> mutableObject = new MutableObject();
                  MutableObject<BlockPos> mutableObject2 = new MutableObject();
                  MutableDouble mutableDouble = new MutableDouble();

                  for (BlockPos blockPos : set3) {
                     if (SpeedmineModule.getSpeedmineModule().testBlockPos(blockPos, clientWorld.getBlockState(blockPos), clientPlayerEntity, clientWorld)
                        && !BlockUtils.testBlockPos9(blockPos, clientWorld)
                        && !clientWorld.getBlockState(blockPos).getCollisionShape(clientWorld, blockPos).isEmpty()
                        && BlockUtils.testBlockPos7(blockPos, clientWorld)) {
                        PlayerEntity playerEntity = PlayerAtPosTracker.this.resolvePlayerEntity(blockPos, clientWorld);
                        if (playerEntity != null) {
                           double d = AutoMineModule.getAutoMineModule()
                              .computeClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerEntity, blockPos, Base.Inside);
                           if (mutableObject2.getValue() == null || d > mutableDouble.getValue()) {
                              mutableObject2.setValue(blockPos);
                              mutableDouble.setValue(d);
                              mutableObject.setValue(playerEntity);
                           }
                        }
                     }
                  }

                  for (BlockPos blockPos2 : set2) {
                     if (SpeedmineModule.getSpeedmineModule().testBlockPos(blockPos2, clientWorld.getBlockState(blockPos2), clientPlayerEntity, clientWorld)) {
                        PlayerEntity playerEntity2 = PlayerAtPosTracker.this.resolvePlayerEntity(blockPos2, clientWorld);
                        if (playerEntity2 != null) {
                           double d2 = AutoMineModule.getAutoMineModule()
                              .computeClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerEntity, blockPos2, Base.Surrounding);
                           if (mutableObject2.getValue() == null || d2 > mutableDouble.getValue()) {
                              mutableObject2.setValue(blockPos2);
                              mutableDouble.setValue(d2);
                              mutableObject.setValue(playerEntity2);
                           }
                        }
                     }
                  }

                  if (mutableObject2.getValue() == null && !clientPlayerEntity.isSwimming()) {
                     for (BlockPos blockPos3 : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getY() + 2.5)) {
                        if (BlockUtils.testBlockPos7(blockPos3, clientWorld)
                           && !BlockUtils.testBlockPos7(blockPos3.up(), clientWorld)
                           && SpeedmineModule.getSpeedmineModule()
                              .testBlockPos(blockPos3, clientWorld.getBlockState(blockPos3), clientPlayerEntity, clientWorld)) {
                           PlayerEntity playerEntity3 = PlayerAtPosTracker.this.resolvePlayerEntity(blockPos3, clientWorld);
                           if (playerEntity3 != null) {
                              double d3 = AutoMineModule.getAutoMineModule()
                                 .computeClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerEntity, blockPos3, Base.Top);
                              if (mutableObject2.getValue() == null || d3 > mutableDouble.getValue()) {
                                 mutableObject2.setValue(blockPos3);
                                 mutableDouble.setValue(d3);
                                 mutableObject.setValue(playerEntity3);
                              }
                           }
                        }
                     }

                     if (mutableObject2.getValue() == null) {
                        for (BlockPos blockPos4 : (Set<BlockPos>)set2.stream().map(BlockPos::up).collect(Collectors.toSet())) {
                           if (BlockUtils.testBlockPos7(blockPos4, clientWorld)
                              && !BlockUtils.testBlockPos7(blockPos4.up(), clientWorld)
                              && SpeedmineModule.getSpeedmineModule()
                                 .testBlockPos(blockPos4, clientWorld.getBlockState(blockPos4), clientPlayerEntity, clientWorld)) {
                              PlayerEntity playerEntity4 = PlayerAtPosTracker.this.resolvePlayerEntity(blockPos4, clientWorld);
                              if (playerEntity4 != null) {
                                 double d4 = AutoMineModule.getAutoMineModule()
                                    .computeClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerEntity, blockPos4, Base.Top);
                                 if (mutableObject2.getValue() == null || d4 > mutableDouble.getValue()) {
                                    mutableObject2.setValue(blockPos4);
                                    mutableDouble.setValue(d4);
                                    mutableObject.setValue(playerEntity4);
                                 }
                              }
                           }
                        }
                     }
                  }

                  BlockPos blockPos5 = (BlockPos)mutableObject2.getValue();
                  if (blockPos5 == null) {
                     phobosClient.getBlockBreakManager().getBlockPosMap3().clear();
                  } else if (!PlayerAtPosTracker.this.testBlockPos(blockPos5)) {
                     phobosClient.getBlockBreakManager().getBlockPosMap3().entrySet().removeIf(entry -> entry.getKey() != blockPos5);
                     phobosClient.getBlockBreakManager().handleBlockPos2(blockPos5, (PlayerEntity)mutableObject.getValue(), 1);
                  }
               }
            }
         }
      );
   }

   @Nullable
   public PlayerEntity resolvePlayerEntity(BlockPos blockPos, World world) {
      for (PlayerEntity playerEntity : world.getPlayers()) {
         if (!playerEntity.isMainPlayer() && !(playerEntity.getEyePos().distanceTo(blockPos.toCenterPos()) > 6.0)) {
            return playerEntity;
         }
      }

      return null;
   }

   private record Entry(BlockPos pos, long timeStamp) {
   }
}
