package org.phobos.core.placement;

import java.util.Map;
import java.util.function.BiPredicate;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.module.PlacementModule;

public class TimedPositionPoolBuilder extends PositionPoolBuilder {
   public final Map<BlockPos, Long> blockPosMap;

   public TimedPositionPoolBuilder(Map<BlockPos, Long> map) {
      this.blockPosMap = map;
   }

   @Override
   public BiPredicate<BlockPos, BlockPos> resolveBiPredicate2(
      PlacementModule placementModule, Block block, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld
   ) {
      BiPredicate<BlockPos, BlockPos> biPredicate = super.resolveBiPredicate2(placementModule, block, clientPlayerEntity, clientWorld);
      return (blockPos, blockPos2) -> !this.blockPosMap.containsKey(blockPos2) && biPredicate.test(blockPos, blockPos2);
   }
}
