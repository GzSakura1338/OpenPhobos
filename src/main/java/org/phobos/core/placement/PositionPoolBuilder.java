package org.phobos.core.placement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.BlockStateAccess;
import org.phobos.core.path.BlockNode;
import org.phobos.core.path.BlockPathFinder;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.task.CancelToken;
import org.phobos.core.path.PathGrid;
import org.phobos.module.PlacementModule;

public class PositionPoolBuilder {
   public final BlockNode blockNode;

   @NotNull
   public List<BlockPos> resolveList(
      BlockPos blockPos, int i, BiPredicate<BlockPos, BlockPos> biPredicate, BiPredicate<BlockPos, @Nullable BlockPos> biPredicate2
   ) {
      this.blockNode.handleBlockPos(blockPos);
      BlockPathFinder blockPathFinder = new BlockPathFinder(this.blockNode, biPredicate, biPredicate2, blockPos, i);
      List<BlockNode> list = blockPathFinder.resolveObject(CancelToken.INSTANCE);
      return list != null
         ? list.stream().map(BlockNode::getMutableBlockPos).<BlockPos>map(BlockPos::toImmutable).collect(Collectors.toList())
         : Collections.emptyList();
   }

   public BiPredicate<BlockPos, @Nullable BlockPos> resolveBiPredicate(
      PlacementModule placementModule, ClientPlayerEntity clientPlayerEntity, Block block, ClientWorld clientWorld
   ) {
      BlockStateAccess.Delegating delegating = new BlockStateAccess.Delegating(clientWorld);
      return (blockPos, blockPos2) -> !placementModule.testBlockPos7(blockPos, clientPlayerEntity)
            && (blockPos2 == null || this.testBlockPos(blockPos2, blockPos, block, placementModule, clientPlayerEntity, delegating))
         ? placementModule.testBlockPos6(blockPos, clientWorld, clientPlayerEntity)
            || placementModule.testBlockPos5(blockPos, null, clientPlayerEntity, clientWorld, null)
         : false;
   }

   public boolean testBlockPos(
      BlockPos blockPos, BlockPos blockPos2, Block block, PlacementModule placementModule, ClientPlayerEntity clientPlayerEntity, BlockStateAccess.Delegating delegating
   ) {
      if (!placementModule.getBooleanValueSetting7().getValue()) {
         return true;
      }

      Direction direction = BlockUtils.resolveDirection2(blockPos, blockPos2);
      delegating.getBlockPosMap().clear();
      BlockState blockState = delegating.getClientWorld().getBlockState(blockPos2);
      if (blockState.isAir()) {
         blockState = block.getDefaultState();
      }

      delegating.getBlockPosMap().put(blockPos2, blockState);
      return placementModule.testBlockPos3(blockPos2, direction, delegating, clientPlayerEntity);
   }

   public BiPredicate<BlockPos, BlockPos> resolveBiPredicate2(
      PlacementModule placementModule, Block block, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld
   ) {
      BlockStateAccess.Delegating delegating = new BlockStateAccess.Delegating(clientWorld);
      return (blockPos, blockPos2) -> {
         if (clientWorld.getWorldBorder().contains(blockPos2) && !placementModule.testBlockPos7(blockPos2, clientPlayerEntity)) {
            VoxelShape voxelShape = block.getDefaultState().getCollisionShape(clientWorld, blockPos2, ShapeContext.absent());
            return !voxelShape.isEmpty() && placementModule.testBlockPos8(blockPos2, block)
               ? false
               : this.testBlockPos(blockPos, blockPos2, block, placementModule, clientPlayerEntity, delegating) || placementModule.alwaysTrue();
         } else {
            return false;
         }
      };
   }

   public PositionPoolBuilder() {
      PathGrid sub001 = new PathGrid();
      sub001.resolveList(10, BlockNode::new);
      this.blockNode = Objects.requireNonNull(sub001.getBlockNode(), "PoolBuilder center was null!");
   }

   @NotNull
   public List<BlockPos> resolveList2(
      BlockPos blockPos, Block block, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, int i, PlacementModule placementModule
   ) {
      BiPredicate<BlockPos, BlockPos> biPredicate = this.resolveBiPredicate2(placementModule, block, clientPlayerEntity, clientWorld);
      BiPredicate<BlockPos, BlockPos> biPredicate2 = this.resolveBiPredicate(placementModule, clientPlayerEntity, block, clientWorld);
      return this.resolveList(blockPos, i, biPredicate, biPredicate2);
   }
}
