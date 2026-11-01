package org.phobos.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.combat.ExplosionDamageCalculator.UnusableBlockStateWorld;

public interface BlockStateAccess extends CollisionView {
   default BlockState getBlockState(BlockPos pos) {
      BlockState blockState = this.getBlockPosMap().get(pos);
      if (blockState == null) {
         blockState = this.getClientWorld().getBlockState(pos);
      }

      return blockState;
   }

   ClientWorld getClientWorld();

   Map<BlockPos, BlockState> getBlockPosMap();

   class BlockStateWorldImpl implements BlockStateAccess {
      public final BlockView blockView;
      public final Map<BlockPos, BlockState> blockPosMap;
      public final ClientWorld clientWorld;

      public FluidState getFluidState(BlockPos pos) {
         return this.clientWorld.getFluidState(pos);
      }

      @Override
      public ClientWorld getClientWorld() {
         return this.clientWorld;
      }

      @Nullable
      public BlockEntity getBlockEntity(BlockPos pos) {
         return this.clientWorld.getBlockEntity(pos);
      }

      public BlockView getBlockView() {
         return this.blockView;
      }

      public WorldBorder getWorldBorder() {
         return this.clientWorld.getWorldBorder();
      }

      public int getHeight() {
         return this.clientWorld.getHeight();
      }

      @Nullable
      public BlockView getChunkAsView(int chunkX, int chunkZ) {
         return this.blockView;
      }

      public BlockStateWorldImpl(ClientWorld clientWorld, Map<BlockPos, BlockState> map, BlockView blockView) {
         this.clientWorld = clientWorld;
         this.blockPosMap = map;
         this.blockView = blockView;
      }

      public BlockStateWorldImpl(ClientWorld clientWorld) {
         this.clientWorld = clientWorld;
         this.blockPosMap = new HashMap<>();
         this.blockView = new BlockStateAccess.BlockStateWorldImpl(clientWorld, this.blockPosMap, clientWorld) {
            @Nullable
            @Override
            public BlockView getChunkAsView(int chunkX, int chunkZ) {
               return BlockStateWorldImpl.this.clientWorld.getChunkAsView(chunkX, chunkZ);
            }
         };
      }

      @Override
      public Map<BlockPos, BlockState> getBlockPosMap() {
         return this.blockPosMap;
      }

      public List<VoxelShape> getEntityCollisions(@Nullable Entity entity, Box box) {
         return this.clientWorld.getEntityCollisions(entity, box);
      }

      public int getBottomY() {
         return this.clientWorld.getBottomY();
      }
   }

   class Delegating extends UnusableBlockStateWorld implements BlockStateAccess {
      public static BlockStateAccess.Delegating createDelegating(ClientWorld clientWorld) {
         return new BlockStateAccess.Delegating(clientWorld);
      }

      @Override
      public int getTopY() {
         return this.getBottomY() + this.getHeight();
      }

      @Override
      public int countVerticalSections() {
         return this.getTopSectionCoord() - this.getBottomSectionCoord();
      }

      public boolean isAir(BlockPos pos) {
         return this.getBlockState(pos).isAir();
      }

      public BlockStateAccess.Delegating resolveDelegating(Consumer<BlockStateAccess.Delegating> consumer) {
         consumer.accept(this);
         return this;
      }

      @Override
      public int getHeight() {
         return this.getDimension().height();
      }

      @Override
      public int getBottomSectionCoord() {
         return ChunkSectionPos.getSectionCoord(this.getBottomY());
      }

      public Delegating(ClientWorld clientWorld) {
         super(clientWorld);
      }

      @Override
      public int getBottomY() {
         return this.getDimension().minY();
      }

      @Override
      public int getTopSectionCoord() {
         return ChunkSectionPos.getSectionCoord(this.getTopY() - 1) + 1;
      }
   }
}
