package org.phobos.module.combat;

import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.tick.TimeUtils;

public class BlockerModule extends PlacementModule {
   public final Setting<Integer> ticks;
   public final Setting<Boolean> air;
   public final Setting<KeyCombination> key;
   public final Setting<BlockerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BlockerModule.Mode.Instant));

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Consumer<BlockPos> consumer, Direction... directions
   ) {
      BlockState blockState;
      if (!(blockState = clientWorld.getBlockState(blockPos)).isOf(Blocks.BEDROCK)) {
         if (!this.air.getValue() && blockState.isAir()) {
            return;
         }

         if (clientPlayerEntity.squaredDistanceTo(blockPos.toCenterPos()) > 16.0) {
            return;
         }

         BlockBreakManager.Progress progress = this.phobosClient.getBlockBreakManager().getBlockPosMap3().get(blockPos);
         switch ((BlockerModule.Mode)this.mode.getValue()) {
            case Broken:
               if (!this.phobosClient.getBlockBreakManager().testBlockPos(blockPos) && progress == null) {
                  return;
               }
               break;
            case Auto:
               if (!this.phobosClient.getBlockBreakManager().testBlockPos(blockPos)) {
                  if (progress == null) {
                     return;
                  }

                  float f;
                  if (!(
                        (f = BlockBreakManager.computeBlockPos(blockPos, blockState, clientWorld))
                                 * ((float)TimeUtils.computeLong(progress.getLongValue()) / 50.0F)
                              + this.ticks.getValue().intValue() * f
                           >= 1.0F
                     )
                     && !this.phobosClient.getBindManager().testKeyCombination(this.key.getValue())) {
                     return;
                  }
               }
            case Ghost:
            default:
               break;
            case Instant:
               if (!this.phobosClient.getBlockBreakManager().testBlockPos(blockPos)) {
                  return;
               }
         }

         for (Direction direction : directions) {
            consumer.accept(blockPos.offset(direction));
         }
      }
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (block != null) {
         Consumer<BlockPos> consumer = blockPos -> this.resolvePlacement8(block, blockPos);
         Set<BlockPos> set = this.phobosClient.getSurroundManager().resolveSet(clientPlayerEntity);
         if (set != null) {
            set.removeIf(blockPos -> blockPos.getY() < clientPlayerEntity.getY() - 0.25);

            for (BlockPos blockPos : set) {
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos, consumer, Direction.values());
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos.up(), consumer, Direction.UP);
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos.up(2), consumer, Direction.DOWN);
            }
         }

         for (BlockPos blockPos2 : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY + 2.5)) {
            this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos2, consumer, Direction.UP);
            this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos2.up(), consumer, Direction.DOWN);
         }

         for (BlockPos blockPos3 : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY - 0.25)) {
            this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos3, consumer, DirectionUtils.directionArray4);
         }
      }
   }

   public BlockerModule(PhobosClient phobosClient) {
      super(phobosClient, "Blocker", "Blocks mining positions around you", Categories.combat, 6);
      this.air = this.resolveSetting2(new BooleanSetting("Air").resolveSetting7("Checks empty blocks"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 3, 0, 10).resolveSetting4(this.mode, BlockerModule.Mode.Auto));
      this.key = this.resolveSetting2(new BindSetting("Key").resolveSetting4(this.mode, BlockerModule.Mode.Auto));
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private enum Mode {
      Instant,
      Auto,
      Ghost,
      Broken;
   }
}
