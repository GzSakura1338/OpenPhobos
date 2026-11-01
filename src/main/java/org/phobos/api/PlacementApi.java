package org.phobos.api;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.module.PlacementModule;

public interface PlacementApi {
   default float computeDirection(Direction direction) {
      return switch (direction) {
         case UP -> -90.0F;
         case DOWN -> 90.0F;
         default -> 5.0F;
      };
   }

   List<PlacementModule.Action> getActionList();

   default boolean testBlockPos(BlockPos blockPos, ClientWorld clientWorld, PlayerEntity playerEntity, double d, boolean flag) {
      if (clientWorld.getWorldBorder().contains(blockPos) && this.testBlockPos4(blockPos, playerEntity, d)) {
         BlockState blockState = clientWorld.getBlockState(blockPos);
         return !flag && blockState.getBlock() instanceof BlockWithEntity ? false : !blockState.isAir() && blockState.getFluidState().isEmpty();
      } else {
         return false;
      }
   }

   default boolean testBlockPos2(
      BlockPos blockPos,
      @Nullable Direction direction,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      @Nullable Set<PlacementModule.Action> set
   ) {
      Iterator var6 = this.getActionList().iterator();

      PlacementModule.Action action;
      while (true) {
         if (!var6.hasNext()) {
            return false;
         }

         action = (PlacementModule.Action)var6.next();
         if (action.getBlockPos2().equals(blockPos)) {
            if (direction == null || this.getDirectionResolver() == DefaultDirectionResolver.defaultDirectionResolver) {
               break;
            }

            if (action.getItem() instanceof BlockItem blockItem) {
               BlockStateAccess.Delegating delegating = new BlockStateAccess.Delegating(clientWorld);
               delegating.getBlockPosMap().put(action.getBlockPos2(), blockItem.getBlock().getDefaultState());
               if (!this.testBlockPos3(action.getBlockPos2(), direction.getOpposite(), delegating, clientPlayerEntity)) {
                  continue;
               }
               break;
            }
         }
      }

      if (set != null) {
         set.add(action);
      }

      return true;
   }

   boolean testBlockPos3(BlockPos var1, Direction var2, ClientWorld var3, PlayerEntity var4);

   @Nullable
   default Block resolveBlock2(ItemSwitcher itemSwitcher) {
      return itemSwitcher.resolveBlock(
         Blocks.BEDROCK,
         Blocks.END_PORTAL_FRAME,
         Blocks.OBSIDIAN,
         Blocks.CRYING_OBSIDIAN,
         Blocks.TRIAL_SPAWNER,
         Blocks.REINFORCED_DEEPSLATE,
         Blocks.ENDER_CHEST
      );
   }

   @Nullable
   default Block resolveBlock3(ItemSwitcher itemSwitcher) {
      return itemSwitcher.resolveBlock(Blocks.OBSIDIAN, Blocks.BEDROCK);
   }

   default boolean testBlockPos4(BlockPos blockPos, PlayerEntity playerEntity, double d) {
      return playerEntity.getPos()
            .squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - playerEntity.getEyeHeight(playerEntity.getPose()), blockPos.getZ() + 0.5)
         <= MathHelper.square(d);
   }

   default float computeDirection2(Direction direction) {
      return switch (direction) {
         case SOUTH -> 0.0F;
         case EAST -> -90.0F;
         case WEST -> 90.0F;
         default -> 180.0F;
      };
   }

   DirectionResolver getDirectionResolver();
}
