package org.phobos.core.placement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class DirectionResolver {
   public abstract boolean testBlockPos(BlockPos var1, Direction var2, ClientWorld var3, PlayerEntity var4);

   public Direction[] values2(BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld) {
      List<Direction> list = new ArrayList<>();

      for (Direction direction : Direction.values()) {
         if (this.testBlockPos(blockPos, direction, clientWorld, playerEntity)) {
            list.add(direction);
         }
      }

      return list.toArray(new Direction[0]);
   }

   @Nullable
   public abstract Direction resolveDirection(BlockPos var1, PlayerEntity var2, ClientWorld var3);

   @Nullable
   public Direction resolveDirection2(BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld, Direction... directions) {
      Set<Direction> set = Set.of(directions);

      for (Direction direction : Direction.values()) {
         if (!set.contains(direction) && this.testBlockPos(blockPos, direction, clientWorld, playerEntity)) {
            return direction;
         }
      }

      return null;
   }
}
