package org.phobos.core.placement;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class DefaultDirectionResolver extends DirectionResolver {
   public static DefaultDirectionResolver defaultDirectionResolver = new DefaultDirectionResolver();

   @Override
   public boolean testBlockPos(BlockPos blockPos, Direction direction, ClientWorld clientWorld, PlayerEntity playerEntity) {
      return true;
   }

   @NotNull
   @Override
   public Direction resolveDirection(BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld) {
      return Direction.DOWN;
   }
}
