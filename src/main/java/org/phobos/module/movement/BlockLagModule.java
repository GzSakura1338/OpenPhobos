package org.phobos.module.movement;

import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.task.ConditionalActions;
import org.phobos.core.task.ConditionalActions;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.module.combat.AutoCrystalModule;

public class BlockLagModule extends PlacementModule {
   public final Setting<Double> offsetValue;
   public final Setting<Boolean> ground;
   public final ConditionalActions conditionalActions;
   public final Setting<Boolean> offset;
   public final Setting<Boolean> disable;
   public final Setting<Boolean> bypassRotate;
   public final Setting<Boolean> skip;
   public final Setting<Boolean> strict;
   public boolean flag;
   public final Setting<BlockLagModule.Mode> mode = new EnumSetting<>("Mode", BlockLagModule.Mode.EnderChest).resolveSetting6(this);
   public int intValue;
   public final Setting<Boolean> bypass;

   public double computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (this.offset.getValue()) {
         return this.offsetValue.getValue();
      }

      for (int i = clientPlayerEntity.getBlockY(); i < clientPlayerEntity.getY() + 10.0; i++) {
         if (BlockUtils.testBlockPos7(new BlockPos((int)clientPlayerEntity.getX(), i, (int)clientPlayerEntity.getZ()), clientWorld)) {
            return i - 1;
         }
      }

      return 3.0;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!(clientPlayerEntity.fallDistance > 0.0F) && clientPlayerEntity.isOnGround()) {
         Block block = switch ((BlockLagModule.Mode)this.mode.getValue()) {
            case Obsidian -> itemSwitcher.resolveBlock(Blocks.ENDER_CHEST);
            case EnderChest -> itemSwitcher.resolveBlock(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);
            case Any -> (Block)itemSwitcher.resolveObject(slot -> slot.getStack().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null);
         };
         if (block != null) {
            if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               BlockPos blockPos = BlockPos.ofFloored(clientPlayerEntity.getPos());
               double d = clientPlayerEntity.getY();
               if (!BlockUtils.testBlockPos7(blockPos, clientWorld) && BlockUtils.testBlockPos7(blockPos.down(), clientWorld)) {
                  if (!this.skip.getValue()) {
                     Set<BlockPos> set = BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY + 2.5);
                     if (!set.stream().filter(blockPosx -> BlockUtils.testBlockPos7(blockPosx, clientWorld)).toList().isEmpty()) {
                        return;
                     }
                  }

                  if (this.testBlockPos9(blockPos, clientWorld)) {
                     if (mc.isInSingleplayer()) {
                        clientWorld.setBlockState(blockPos, block.getDefaultState(), 512);
                        this.conditionalActions.run();
                     } else {
                        VoxelShape voxelShape = block.getDefaultState()
                           .getOutlineShape(clientWorld, blockPos)
                           .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                        for (Entity entity : clientWorld.getOtherEntities(null, voxelShape.getBoundingBox())) {
                           if (entity.intersectionChecked
                              && !entity.isRemoved()
                              && VoxelShapes.matchesAnywhere(
                                 voxelShape, VoxelShapes.cuboid(BoxUtils.createBox4(entity.getBoundingBox())), BooleanBiFunction.AND
                              )) {
                              if (entity instanceof EndCrystalEntity endCrystalEntity) {
                                 AutoCrystalModule.handleEndCrystalEntity(endCrystalEntity, true);
                              } else if (!entity.equals(clientPlayerEntity)) {
                                 return;
                              }
                           }
                        }

                        if (this.bypass.getValue() && this.bypassRotate.getValue()) {
                           if (!this.flag) {
                              this.phobosClient.getRotationManager().testFloat(clientPlayerEntity.getYaw(), 90.0F);
                              this.flag = true;
                              this.intValue = 0;
                              return;
                           }

                           if (this.intValue++ < 3) {
                              return;
                           }
                        }

                        this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getX(), d + 0.4, clientPlayerEntity.getZ(), false);
                        this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getX(), d + 0.75, clientPlayerEntity.getZ(), false);
                        this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getX(), d + 1.01, clientPlayerEntity.getZ(), false);
                        this.handleClientPlayerEntity(
                           clientPlayerEntity, clientPlayerEntity.getX(), d + (this.bypass.getValue() ? 0.99999992 : 1.15), clientPlayerEntity.getZ(), false
                        );
                        this.resolvePlacement6(block.asItem(), blockPos, null, false);
                        this.handleItemSwitcher2(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, true);
                        this.flag = false;
                        if (this.bypass.getValue()) {
                           this.handleClientPlayerEntity(
                              clientPlayerEntity, clientPlayerEntity.getX(), d + 1.15, clientPlayerEntity.getZ(), clientPlayerEntity.isOnGround()
                           );
                           PlayerActionUtils.handleClientPlayerEntity11(clientPlayerEntity);
                           clientPlayerEntity.setPos(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ());
                           this.conditionalActions.run();
                        } else {
                           this.handleClientPlayerEntity(
                              clientPlayerEntity,
                              clientPlayerEntity.getX(),
                              this.strict.getValue() ? d - 10.0 : d + this.computeClientPlayerEntity(clientPlayerEntity, clientWorld),
                              clientPlayerEntity.getZ(),
                              this.ground.getValue()
                           );
                           this.conditionalActions.run();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public BlockLagModule(PhobosClient phobosClient) {
      super(phobosClient, "BlockLag", "Places a block inside of you", Categories.movement, 8);
      this.disable = new BooleanSetting("Disable", true).resolveSetting7("Disables the module after execution").resolveSetting6(this);
      this.ground = new BooleanSetting("Ground").resolveSetting7("Packet isOnGround parameter").resolveSetting6(this);
      this.offset = new BooleanSetting("Offset").resolveSetting7("Static offset").resolveSetting6(this);
      this.offsetValue = new NumberSetting<>("OffsetValue", 2.0, -10.0, 10.0).resolveSetting6(this);
      this.strict = new BooleanSetting("Strict").resolveSetting7("Strict offset").resolveSetting6(this);
      this.bypass = new BooleanSetting("Bypass").resolveSetting7("Bypass").resolveSetting6(this);
      this.bypassRotate = new BooleanSetting("BypassRotate").resolveSetting6(this);
      this.skip = new BooleanSetting("Skip").resolveSetting7("Ignores blocks on your head").resolveSetting6(this);
      this.conditionalActions = new ConditionalActions.Builder().resolveBuilder(this::run11, this.disable::getValue).getConditionalActions();
      this.resolveSettingGroup(this.offset, this.offsetValue);
      this.resolveSettingGroup(this.bypass, this.bypassRotate);
   }

   public boolean testBlockPos9(BlockPos blockPos, ClientWorld clientWorld) {
      return clientWorld.isInBuildLimit(blockPos);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3, boolean flag) {
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(d, d2, d3, flag));
   }

   @Nullable
   public BlockPos resolveBlockPos(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Set<BlockPos> set, Block block) {
      for (BlockPos blockPos : set) {
         if (BlockUtils.testClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos, block)) {
            return blockPos;
         }
      }

      return null;
   }

   private enum Mode {
      EnderChest,
      Obsidian,
      Any;
   }
}
