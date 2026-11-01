package org.phobos.module.combat;

import java.awt.Color;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.module.PlacementModule;
import org.phobos.core.render.Highlights;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class PusherModule extends PlacementModule {
   public final Setting<Boolean> phase;
   public final Setting<InteractionManager.Silent> crystalSilent;
   public final TimeGate.Normal normal3;
   public final Setting<Boolean> crystalOnly;
   public final Setting<PusherModule.Placement> placement;
   public final Setting<Boolean> crystalExtra;
   public final Setting<Boolean> crystal = this.resolveSetting2(new BooleanSetting("Crystal").resolveSetting7("Pushes crystals into enemies"));
   public final Setting<PusherModule.Axis> crystalAxis;
   public final Setting<InteractionManager.SwitchMode> crystalSwitch;
   public final Setting<PusherModule.Verification> verify;
   public final Setting<Long> delay;

   public boolean testClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      Block block,
      Block block2,
      ClientWorld clientWorld,
      BlockPos blockPos,
      BlockPos blockPos2,
      Direction direction,
      boolean flag,
      boolean flag2,
      boolean flag3,
      int... ints
   ) {
      Direction direction4 = direction.getOpposite();
      if (ints.length > 0) {
         for (Entity entity : clientWorld.getOtherEntities(null, BoxUtils.createBox9(blockPos.up()))) {
            if (entity instanceof EndCrystalEntity endCrystalEntity
               && (
                  clientPlayerEntity.isCreative()
                     || !(this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, clientWorld, endCrystalEntity) >= 8.0F)
               )
               && AutoCrystalModule.getAutoCrystalModule().testClientPlayerEntity4(clientPlayerEntity, clientWorld, endCrystalEntity)) {
               AutoCrystalModule.getAutoCrystalModule().handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, true);
               break;
            }
         }

         BlockPos blockPos4 = this.resolveBlockPos3(clientWorld, blockPos, direction);
         if (blockPos4 != null) {
            BlockPos blockPos3 = this.resolveBlockPos2(blockPos4, direction4);
            if (blockPos3 != null) {
               this.setActionConsumer(
                  action -> action.setRunnable(
                     () -> AutoCrystalModule.getAutoCrystalModule()
                        .handleClientPlayerEntity2(
                           clientPlayerEntity, clientWorld, blockPos, this.crystalSwitch.getValue(), this.crystalSilent.getValue(), true
                        )
                  )
               );
               return this.resolvePlacement8(block2, blockPos3).isSuccessful();
            } else {
               return false;
            }
         } else {
            BlockPos blockPos5 = this.resolveBlockPos(clientWorld, blockPos, direction, blockPosx -> this.resolvePlacement9(block, blockPosx).isSuccessful());
            if (blockPos5 == null) {
               return false;
            }

            if (this.testClientWorld3(clientWorld, direction4, blockPos5)) {
               this.setActionConsumer(
                  action -> action.setRunnable(
                     () -> AutoCrystalModule.getAutoCrystalModule()
                        .handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, this.crystalSwitch.getValue(), this.crystalSilent.getValue())
                  )
               );
               this.resolvePlacement7(block, blockPos5, direction.getOpposite());
               return true;
            }

            BlockPos blockPos6 = this.resolveBlockPos2(blockPos5, direction4);
            if (blockPos6 == null) {
               return false;
            }

            switch ((PusherModule.Placement)this.placement.getValue()) {
               case Redstone:
                  this.resolvePlacement8(block2, blockPos6);
                  this.setActionConsumer(
                     action -> action.setRunnable(
                        () -> AutoCrystalModule.getAutoCrystalModule()
                           .handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, this.crystalSwitch.getValue(), this.crystalSilent.getValue())
                     )
                  );
                  this.resolvePlacement7(block, blockPos5, direction.getOpposite());
                  break;
               case Piston:
                  this.resolvePlacement7(block, blockPos5, direction.getOpposite());
                  this.setActionConsumer(
                     action -> action.setRunnable(
                        () -> AutoCrystalModule.getAutoCrystalModule()
                           .handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, this.crystalSwitch.getValue(), this.crystalSilent.getValue())
                     )
                  );
                  this.resolvePlacement8(block2, blockPos6);
            }

            return true;
         }
      } else {
         if (this.crystal.getValue()
            && !flag2
            && this.testDirection(direction)
            && BlockUtils.testBlockPos(blockPos, clientWorld, false)
            && this.testClientPlayerEntity(clientPlayerEntity, block, block2, clientWorld, blockPos, blockPos2, direction, flag, flag3, false, 1)) {
            return true;
         }

         if (!this.crystalOnly.getValue() && !flag && flag3 && (flag2 || !this.phase.getValue())) {
            if (BlockUtils.testBlockPos7(blockPos.offset(direction), clientWorld)) {
               return false;
            }

            BlockState blockState = clientWorld.getBlockState(blockPos);
            if (blockState.getBlock() instanceof PistonBlock) {
               if (blockState.get(FacingBlock.FACING) != direction) {
                  return false;
               }

               for (Direction direction2 : Direction.values()) {
                  if (clientWorld.getBlockState(blockPos.offset(direction2)).getBlock() instanceof RedstoneBlock) {
                     return true;
                  }
               }

               for (Direction direction3 : Direction.values()) {
                  if (!this.resolvePlacement8(block2, blockPos.offset(direction3)).isFailed()) {
                     return true;
                  }
               }

               return false;
            } else if (this.resolvePlacement9(block, blockPos).isFailed()) {
               return false;
            } else if (this.testClientWorld3(clientWorld, null, blockPos)) {
               this.resolvePlacement7(block, blockPos, direction4);
               return true;
            } else {
               BlockPos blockPos7 = this.resolveBlockPos2(blockPos, null);
               if (blockPos7 != null) {
                  this.resolvePlacement7(block, blockPos, direction4);
                  this.resolvePlacement8(block2, blockPos7);
                  return true;
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      }
   }

   public PusherModule(PhobosClient phobosClient) {
      super(phobosClient, "Pusher", Categories.combat, 4);
      this.crystalOnly = this.resolveSetting2(new BooleanSetting("CrystalOnly"));
      this.crystalAxis = this.resolveSetting2(new EnumSetting<>("CrystalAxis", PusherModule.Axis.Any));
      this.crystalExtra = this.resolveSetting2(new BooleanSetting("CrystalExtra"));
      this.crystalSwitch = this.resolveSetting2(new EnumSetting<>("CrystalSwitch", InteractionManager.SwitchMode.Silent));
      this.crystalSilent = this.resolveSetting2(new EnumSetting<>("CrystalSilent", InteractionManager.Silent.Normal));
      this.verify = this.resolveSetting2(new EnumSetting<>("Verify", PusherModule.Verification.Simulation));
      this.placement = this.resolveSetting2(new EnumSetting<>("Placement", PusherModule.Placement.Redstone).resolveSetting7("First block to place"));
      this.phase = this.resolveSetting2(new BooleanSetting("Phase").resolveSetting7("Only push players when they are phasing"));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 500L, 0L, 5000L));
      this.normal3 = new TimeGate.Normal();
      this.resolveSettingGroup(this.crystal, this.crystalOnly, this.crystalAxis, this.crystalExtra, this.crystalSwitch, this.crystalSilent);
   }

   public boolean testBlockPos9(BlockPos blockPos, ClientWorld clientWorld) {
      return !clientWorld.getBlockState(blockPos).getCollisionShape(clientWorld, blockPos).isEmpty();
   }

   public boolean testClientWorld(ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      return !BlockUtils.testBlockPos7(blockPos.offset(direction), clientWorld);
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld, BlockPos blockPos, BlockPos blockPos2, Direction direction) {
      return switch ((PusherModule.Verification)this.verify.getValue()) {
         case Simulation -> {
            Box box = playerEntity.getBoundingBox();
            Box box2 = BoxUtils.createBox9(blockPos2.offset(direction));

            double d = switch (direction) {
               case EAST -> box.maxX - box2.minX;
               case WEST -> box2.maxX - box.minX;
               case SOUTH -> box.maxZ - box2.minZ;
               case NORTH -> box2.maxZ - box.minZ;
               case UP -> box.maxY - box2.minY;
               case DOWN -> box2.maxY - box.minY;
               default -> throw new MatchException(null, null);
            };
            double d2 = Math.max(0.0, Math.min(1.0, d));
            Box box3 = box.offset(direction.getOffsetX() * d2, direction.getOffsetY() * d2, direction.getOffsetZ() * d2);
            yield BlockPosUtils.resolveSet6(box3).stream().noneMatch(blockPosx -> this.testBlockPos9(blockPosx, clientWorld));
         }
         case Simple -> {
            Box box4 = playerEntity.getBoundingBox();

            double d3 = switch (direction) {
               case EAST -> box4.maxX - (blockPos2.getX() + 1);
               case WEST -> blockPos2.getX() - box4.minX + 1.0;
               case SOUTH -> box4.maxZ - (blockPos2.getZ() + 1);
               case NORTH -> blockPos2.getZ() - box4.minZ + 1.0;
               case UP -> box4.maxY - (blockPos2.getY() + 1);
               case DOWN -> blockPos2.getY() - box4.minY + 1.0;
               default -> throw new MatchException(null, null);
            };
            double d4 = MathUtils.computeDouble5(d3, 0.0, 1.0);
            if (d4 <= 0.0) {
               yield false;
            } else {
               Vec3d vec3d = new Vec3d(direction.getOffsetX() * d4, direction.getOffsetY() * d4, direction.getOffsetZ() * d4);
               MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
               movingFakePlayer.copyFrom(playerEntity);
               movingFakePlayer.move(MovementType.SELF, vec3d);
               if (this.check6()) {
                  Highlights.handleBox(movingFakePlayer.getBoundingBox(), Color.RED, 250L);
                  Highlights.handleBox(playerEntity.getBoundingBox(), Color.BLUE, 250L);
               }

               BlockPos blockPos3 = BlockPos.ofFloored(movingFakePlayer.getPos());
               yield blockPos3.equals(BlockPos.ofFloored(playerEntity.getPos()))
                  ? false
                  : clientWorld.getBlockState(blockPos3).getCollisionShape(clientWorld, blockPos3).isEmpty();
            }
         }
      };
   }

   @Nullable
   public BlockPos resolveBlockPos(ClientWorld clientWorld, BlockPos blockPos, Direction direction, Predicate<BlockPos> predicate) {
      BlockPos[] blockPoss = this.values(blockPos, direction);

      for (BlockPos blockPos2 : blockPoss) {
         if (!BlockUtils.testBlockPos7(blockPos2, clientWorld) && this.testClientWorld(clientWorld, blockPos2, direction) && predicate.test(blockPos2)) {
            return blockPos2;
         }
      }

      return null;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = itemSwitcher.resolveBlock(Blocks.PISTON, Blocks.STICKY_PISTON);
      Block block2 = itemSwitcher.resolveBlock(Blocks.REDSTONE_BLOCK);
      if (block != null && block2 != null) {
         for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
            if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 8.0) && playerEntity.isOnGround()) {
               boolean flag2 = this.phobosClient.getSurroundManager().testPlayerEntity2(playerEntity);
               boolean flag3 = BlockUtils.testPlayerEntity(playerEntity, clientWorld);
               boolean flag4 = playerEntity.isInSwimmingPose();
               if (this.crystal.getValue()) {
                  for (Entity entity : clientWorld.getOtherEntities(null, playerEntity.getBoundingBox().stretch(0.0, 3.0, 0.0))) {
                     if (entity instanceof EndCrystalEntity endCrystalEntity
                        && !this.testEndCrystalEntity(endCrystalEntity)
                        && (
                           clientPlayerEntity.isCreative()
                              || !(this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, clientWorld, endCrystalEntity) >= 8.0F)
                        )
                        && AutoCrystalModule.getAutoCrystalModule().testClientPlayerEntity4(clientPlayerEntity, clientWorld, endCrystalEntity)) {
                        AutoCrystalModule.getAutoCrystalModule().handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, true);
                     }
                  }
               }

               if (this.normal3.testNumber(this.delay.getValue())
                  && !BlockUtils.testClientWorld(clientWorld, playerEntity, (blockPosx, blockState) -> blockState.isOf(Blocks.COBWEB))) {
                  for (BlockPos blockPos3 : BlockPosUtils.resolveSet3(playerEntity, playerEntity.getY() + 0.25)) {
                     for (Direction direction : DirectionUtils.directionArray) {
                        BlockPos blockPos = blockPos3.offset(direction.getOpposite());
                        BlockPos blockPos2 = blockPos3.offset(direction);
                        boolean flag = !flag4 || !flag3;
                        if (this.testClientPlayerEntity(
                              clientPlayerEntity,
                              block,
                              block2,
                              clientWorld,
                              blockPos,
                              blockPos2,
                              direction,
                              flag2,
                              flag3,
                              flag && this.testPlayerEntity(playerEntity, clientWorld, blockPos3, blockPos, direction)
                           )
                           || this.testClientPlayerEntity(
                              clientPlayerEntity,
                              block,
                              block2,
                              clientWorld,
                              blockPos.up(),
                              blockPos2.up(),
                              direction,
                              flag2,
                              flag4 || flag3,
                              flag && this.testPlayerEntity(playerEntity, clientWorld, blockPos3, blockPos.up(), direction)
                           )) {
                           this.normal3.run();
                           return;
                        }

                        if (!flag4
                           && this.crystal.getValue()
                           && this.crystalExtra.getValue()
                           && this.testClientPlayerEntity(
                              clientPlayerEntity, block, block2, clientWorld, blockPos.up().up(), blockPos2.up().up(), direction, flag2, flag3, false
                           )) {
                           this.normal3.run();
                           return;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean testClientWorld2(ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      return blockState.getBlock() instanceof PistonBlock && ((Direction)blockState.get(FacingBlock.FACING)).equals(direction);
   }

   @Nullable
   public BlockPos resolveBlockPos2(BlockPos blockPos, Direction direction, BlockPos... blockPoss) {
      if (direction != null) {
         BlockPos blockPos2 = blockPos.offset(direction);
         return this.resolvePlacement9(Blocks.REDSTONE_BLOCK, blockPos2).isSuccessful() ? blockPos2 : null;
      }

      Set<BlockPos> set = Set.of(blockPoss);

      for (Direction direction2 : Direction.values()) {
         BlockPos blockPos3 = blockPos.offset(direction2);
         if (!set.contains(blockPos3) && !this.resolvePlacement9(Blocks.REDSTONE_BLOCK, blockPos3).isFailed()) {
            return blockPos3;
         }
      }

      return null;
   }

   public boolean testEndCrystalEntity(EndCrystalEntity endCrystalEntity) {
      return this.testDouble(endCrystalEntity.getX()) && this.testDouble(endCrystalEntity.getZ());
   }

   public boolean testDirection(Direction direction) {
      return switch ((PusherModule.Axis)this.crystalAxis.getValue()) {
         case Any -> true;
         case Best -> direction.getAxis().equals(net.minecraft.util.math.Direction.Axis.X);
      };
   }

   @Nullable
   public BlockPos resolveBlockPos3(ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      BlockPos[] blockPoss = this.values(blockPos, direction);

      for (BlockPos blockPos2 : blockPoss) {
         if (this.testClientWorld2(clientWorld, blockPos2, direction)) {
            return blockPos2;
         }
      }

      return null;
   }

   public boolean testClientWorld3(ClientWorld clientWorld, Direction direction, BlockPos blockPos) {
      if (direction != null) {
         return clientWorld.getBlockState(blockPos.offset(direction)).getBlock() instanceof RedstoneBlock;
      }

      for (Direction direction2 : Direction.values()) {
         if (clientWorld.getBlockState(blockPos.offset(direction2)).getBlock() instanceof RedstoneBlock) {
            return true;
         }
      }

      return false;
   }

   public boolean testDouble(double d) {
      return d * 2.0 % 2.0 == 1.0;
   }

   public BlockPos[] values(BlockPos blockPos, Direction direction) {
      BlockPos blockPos2 = blockPos.up().offset(direction.getOpposite());
      BlockPos blockPos3 = blockPos2.offset(DirectionUtils.resolveDirection2(direction));
      BlockPos blockPos4 = blockPos2.offset(DirectionUtils.resolveDirection(direction));
      return new BlockPos[]{blockPos2, blockPos3, blockPos4, blockPos2.up(), blockPos3.up(), blockPos4.up()};
   }

   private enum Axis {
      Any,
      Best;
   }

   private enum Placement {
      Redstone,
      Piston;
   }

   private enum Verification {
      Simple,
      Simulation;
   }
}
