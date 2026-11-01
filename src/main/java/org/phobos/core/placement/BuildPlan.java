package org.phobos.core.placement;

import java.util.Map;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.PositionHolder;
import org.phobos.api.BlockCollection;
import org.phobos.core.block.BlockList;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.util.NullSafe;

public class BuildPlan {
   public boolean flag;
   public final Map<PositionHolder, BlockCollection> positionHolderMap = new ConcurrentHashMap<>();

   public void handleBlockPos(BlockPos blockPos, BiConsumer<BlockPos, BlockCollection> biConsumer) {
      for (Map.Entry<PositionHolder, BlockCollection> entry : this.positionHolderMap.entrySet()) {
         biConsumer.accept(blockPos.add(entry.getKey().getVec3i()), entry.getValue());
      }
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Nullable
   public BlockCollection resolveBlockCollection(Vec3i vec3i) {
      PositionHolder positionHolder = this.resolvePositionHolder(vec3i);
      return positionHolder != null ? this.positionHolderMap.get(positionHolder) : null;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public BuildPlan getBuildPlan() {
      BuildPlan.Builder builder = createBuilder();

      for (Map.Entry<PositionHolder, BlockCollection> entry : this.positionHolderMap.entrySet()) {
         PositionHolder positionHolder = entry.getKey();
         BlockCollection blockCollection = entry.getValue();
         Vec3i vec3i = positionHolder.getVec3i();
         Vec3i vec3i2 = new Vec3i(vec3i.getZ(), vec3i.getY(), vec3i.getX());
         builder.resolveBuilder3(vec3i2.getZ(), vec3i2.getY(), vec3i2.getX(), blockCollection);
      }

      return builder.getBuildPlan();
   }

   public BuildPlan getBuildPlan2() {
      this.setFlag(true);
      return this;
   }

   public boolean testBlockPos(BlockPos blockPos, World world) {
      for (Map.Entry<PositionHolder, BlockCollection> entry : this.positionHolderMap.entrySet()) {
         PositionHolder positionHolder = entry.getKey();
         BlockCollection blockCollection = entry.getValue();
         BlockPos blockPos2 = blockPos.add(positionHolder.getVec3i());
         BlockState blockState = world.getBlockState(blockPos2);

         for (Block block : blockCollection) {
            if (block.equals(Blocks.AIR) && !blockState.isAir()) {
               return false;
            }

            if (blockState.isAir() || blockState.isOf(block)) {
               break;
            }
         }

         if (BlockUtils.testBlockPos11(blockPos2, blockState.getCollisionShape(world, blockPos2), world, false)) {
            return false;
         }
      }

      return true;
   }

   @Nullable
   public PositionHolder resolvePositionHolder(Vec3i vec3i) {
      for (PositionHolder positionHolder : this.positionHolderMap.keySet()) {
         if (positionHolder.testVec3i(vec3i)) {
            return positionHolder;
         }
      }

      return null;
   }

   public static BuildPlan.Builder createBuilder() {
      return new BuildPlan.Builder();
   }

   public void handleVec3i(Vec3i vec3i, BlockCollection blockCollection) {
      NullSafe.handleObject2(this.resolvePositionHolder(vec3i), positionHolder -> {
         BlockCollection blockCollection2 = this.positionHolderMap.get(positionHolder);
         if (blockCollection2 != null) {
            blockCollection2.handleCollection(blockCollection.getBlockList());
         }
      }, () -> this.positionHolderMap.put(new PositionRef(vec3i), blockCollection));
   }

   public void handleVec3i2(Vec3i vec3i, Block block) {
      NullSafe.handleObject2(this.resolvePositionHolder(vec3i), positionHolder -> {
         BlockCollection blockCollection = this.positionHolderMap.get(positionHolder);
         if (blockCollection != null) {
            blockCollection.handleBlock(block);
         }
      }, () -> this.positionHolderMap.put(new PositionRef(vec3i), new BlockList(block)));
   }

   public Map<PositionHolder, BlockCollection> getPositionHolderMap() {
      return this.positionHolderMap;
   }

   public BuildPlan() {
      this.flag = false;
   }

   public static class Builder {
      public final BuildPlan buildPlan = new BuildPlan();

      public BuildPlan.Builder resolveBuilder(Vec3i vec3i, Block block) {
         this.buildPlan.handleVec3i2(vec3i, block);
         return this;
      }

      public BuildPlan.Builder resolveBuilder2(int i, int i2, int i3, Block block) {
         this.buildPlan.handleVec3i2(new Vec3i(i, i2, i3), block);
         return this;
      }

      public BuildPlan.Builder resolveBuilder3(int i, int i2, int i3, BlockCollection blockCollection) {
         this.buildPlan.handleVec3i(new Vec3i(i, i2, i3), blockCollection);
         return this;
      }

      public BuildPlan getBuildPlan() {
         return this.buildPlan.getBuildPlan2();
      }
   }
}
