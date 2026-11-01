package org.phobos.core.placement;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.module.PlacementModule;

public class PredicatePositionPoolBuilder extends PositionPoolBuilder {
   public final Predicate<BlockPos> blockPosPredicate;

   @Override
   public BiPredicate<BlockPos, BlockPos> resolveBiPredicate2(
      PlacementModule placementModule, Block block, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld
   ) {
      BiPredicate<BlockPos, BlockPos> biPredicate = super.resolveBiPredicate2(placementModule, block, clientPlayerEntity, clientWorld);
      return (blockPos, blockPos2) -> !this.blockPosPredicate.test(blockPos2) && biPredicate.test(blockPos, blockPos2);
   }

   public PredicatePositionPoolBuilder(Predicate<BlockPos> predicate) {
      this.blockPosPredicate = predicate;
   }
}
