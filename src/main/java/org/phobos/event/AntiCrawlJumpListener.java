package org.phobos.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.module.player.AntiCrawlModule;
import org.phobos.module.player.ClipModule;
import org.phobos.module.Module;

public final class AntiCrawlJumpListener extends ModuleListener.Safe<AntiCrawlModule, JumpEvent3> {
   public int computeBlockPos(BlockPos blockPos, ClientWorld clientWorld) {
      int i = 0;
      BlockState blockState = clientWorld.getBlockState(blockPos);
      if (blockState.getBlock() == Blocks.BEDROCK) {
         i += 2;
      }

      if (blockState.getBlock() == Blocks.OBSIDIAN) {
         i++;
      }

      if (!clientWorld.getBlockState(blockPos.down()).isReplaceable()) {
         i += 2;
      }

      if (!clientWorld.getBlockState(blockPos.up()).isReplaceable()) {
         i--;
      }

      return i;
   }

   public AntiCrawlJumpListener(AntiCrawlModule antiCrawlModule) {
      super(antiCrawlModule, JumpEvent3.class);
   }

   public boolean testClientWorld(ClientWorld clientWorld, List<BlockPos> list) {
      if (list.size() == 1) {
         BlockPos blockPos = list.getFirst();
         return !BlockUtils.testBlockPos7(blockPos.up(), clientWorld);
      } else {
         return !BlockUtils.testBlockPos7(list.get(0).up(), clientWorld) || !BlockUtils.testBlockPos7(list.get(1).up(), clientWorld);
      }
   }

   public void handleObject(
      JumpEvent3 jumpEvent3, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient
         .getScheduler()
         .handleRunnable2(
            () -> {
               if (clientPlayerEntity.isInSwimmingPose() && !clientPlayerEntity.isTouchingWater()) {
                  Box box2 = clientPlayerEntity.getBoundingBox().expand(0.2F);
                  Map<BlockPos, Integer> map = new HashMap<>();
                  BlockPos blockPos3 = clientPlayerEntity.getBlockPos();

                  for (Direction direction : DirectionUtils.directionArray) {
                     if (!clientWorld.getBlockState(blockPos3.offset(direction)).getCollisionShape(clientWorld, blockPos3.offset(direction)).isEmpty()) {
                        BlockPos blockPos = blockPos3.offset(direction);
                        if (new Box(blockPos).intersects(box2)) {
                           map.put(blockPos, this.computeBlockPos(blockPos, clientWorld));
                        }
                     }
                  }

                  Map<BlockPos, Integer> map2 = map.entrySet()
                     .stream()
                     .sorted(Map.Entry.<BlockPos, Integer>comparingByValue().reversed())
                     .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (integer, integer2) -> integer, LinkedHashMap::new));
                  if (BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
                     return;
                  }

                  List<BlockPos> list = new ArrayList<>(4);

                  for (Map.Entry<BlockPos, Integer> entry : map2.entrySet()) {
                     BlockPos blockPos2 = entry.getKey();
                     Box box = new Box(blockPos2);
                     if (box.intersects(box2)) {
                        list.add(entry.getKey());
                     }
                  }

                  if (list.isEmpty()) {
                     return;
                  }

                  if (!this.testClientWorld(clientWorld, list)) {
                     return;
                  }

                  this.phobosClient.getModuleManager().resolveOptional2(ClipModule.class).ifPresent(Module::run12);
               }
            },
            this.module.ticks.getValue()
         );
   }
}
