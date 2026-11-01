package org.phobos.api;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.placement.PositionPoolBuilder;
import org.phobos.core.math.MathUtils;

public interface AreaScanner {
   class Area implements Iterable<BlockPos> {
      public final Set<BlockPos> blockPosSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

      public List<BlockPos> resolveList(ClientPlayerEntity clientPlayerEntity) {
         return this.blockPosSet.stream().sorted(Comparator.comparingDouble(blockPos -> clientPlayerEntity.squaredDistanceTo(blockPos.toCenterPos()))).toList();
      }

      @NotNull
      @Override
      public Iterator<BlockPos> iterator() {
         return this.blockPosSet.iterator();
      }

      public void handleVec3d(Vec3d vec3d, double d, Predicate<BlockPos> predicate) {
         BlockPos blockPos2 = BlockPos.ofFloored(vec3d);
         int i4 = (int)d;

         for (int i3 = -i4; i3 < d; i3++) {
            for (int i2 = -i4; i2 < i4; i2++) {
               for (int i = -i4; i < i4; i++) {
                  BlockPos blockPos = blockPos2.add(i3, i, i2);
                  if (blockPos2.getSquaredDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= d * d && predicate.test(blockPos)) {
                     this.blockPosSet.add(blockPos);
                  }
               }
            }
         }
      }

      public void handleInt(int i, long l) {
         if (!this.blockPosSet.isEmpty()) {
            List<BlockPos> list = new ArrayList<>(this.blockPosSet);
            Random random = new Random(l);
            int i3 = this.blockPosSet.size();
            Set<BlockPos> set = new HashSet<>();
            Set<Integer> set2 = new HashSet<>();

            while (set2.size() < i) {
               int i2 = random.nextInt(i3);
               if (set2.add(i2)) {
                  set.add(list.get(i2));
               }
            }

            this.blockPosSet.clear();
            this.blockPosSet.addAll(set);
         }
      }

      public void handleConsumer(Consumer<BlockPos> consumer) {
         for (BlockPos blockPos : this) {
            consumer.accept(blockPos);
         }
      }

      public void handleBlockPos(BlockPos blockPos) {
         this.blockPosSet.add(blockPos);
      }

      public void handleInt2(int i) {
         this.blockPosSet.removeIf(blockPos -> blockPos.getX() != i);
      }

      public void handleInt3(int i) {
         this.blockPosSet.removeIf(blockPos -> blockPos.getZ() != i);
      }

      public void handleConsumer2(Consumer<BlockPos> consumer) {
         this.blockPosSet.stream().findAny().ifPresent(consumer);
      }

      public void handleInt4(int i) {
         this.blockPosSet.removeIf(blockPos -> blockPos.getY() != i);
      }

      public Set<BlockPos> getBlockPosSet() {
         return this.blockPosSet;
      }

      public void handleBlockPos2(BlockPos blockPos, Direction direction, int i) {
         for (int i2 = 0; i2 < i; i2++) {
            this.blockPosSet.add(blockPos.offset(direction, i2));
         }
      }

      public void handleBlockPos3(BlockPos blockPos, int i) {
         this.handleVec3d(blockPos.toCenterPos(), i, Predicates.alwaysTrue());
      }

      @Nullable
      public BlockPos resolveBlockPos(boolean flag) {
         return flag ? this.blockPosSet.stream().findFirst().orElse(null) : this.blockPosSet.stream().findAny().orElse(null);
      }

      public void handleBlockPos4(BlockPos blockPos, float f, int i, PositionPoolBuilder positionPoolBuilder) {
         Vec3d vec3d = blockPos.toCenterPos();
         Vec3d vec3d2 = MathUtils.createVec3d4(f, 0.0F);
         Vec3d vec3d3 = vec3d.add(vec3d2.x * i, vec3d2.y * i, vec3d2.z * i);
         BlockPos blockPos2 = BlockPos.ofFloored(vec3d3);
         this.blockPosSet
            .addAll(positionPoolBuilder.resolveList(blockPos2, 10, (blockPosx, blockPos2x) -> true, (blockPos2x, blockPos3) -> blockPos2x.equals(blockPos2)));
      }
   }
}
