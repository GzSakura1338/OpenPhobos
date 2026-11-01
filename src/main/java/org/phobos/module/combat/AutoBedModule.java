package org.phobos.module.combat;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.block.BlockPosGrid;
import org.phobos.core.task.LockedExecutor;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.PhobosClient;
import org.phobos.module.ExplosivePlacementModule;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.event.SafeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoBedModule extends ExplosivePlacementModule {
   public final Setting<Float> maxSelfDamage;
   public final TimeGate.Normal normal3;
   public final Setting<Long> placeDelay;
   public final LockedExecutor lockedExecutor;
   public final Setting<Boolean> suicide;
   public float floatValue;
   public final TimeGate.Normal normal4;
   public final BlockPosGrid<AutoBedModule.BedPosition> bedPositionBlockPosGrid;
   public final Setting<Float> minDamage = this.resolveSetting2(new NumberSetting<>("MinDamage", 4.0F, 0.0F, 36.0F));
   public final MutableObject<AutoBedModule.BedPosition> bedPositionMutableObject;
   public final MutableObject<Float> floatValueMutableObject;
   public final Setting<Long> placeDelay2;

   public void handleItemSwitcher3(ItemSwitcher itemSwitcher, BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      Item item = itemSwitcher.resolveObject(
         slot -> slot.getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BedBlock ? blockItem.asItem() : null
      );
      if (item != null) {
         ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 3);
         if (switchResult != null) {
            this.handleSwitchResult(switchResult, clientPlayerEntity, clientWorld, blockPos);
         }
      }
   }

   public Box createBox(BlockPos blockPos, Direction direction) {
      Box box = new Box(blockPos);
      Box box2 = new Box(blockPos.offset(direction));
      return new Box(
         Math.min(box.minX, box2.minX),
         Math.min(box.minY, box2.minY),
         Math.min(box.minZ, box2.minZ),
         Math.max(box.maxX, box2.maxX),
         Math.max(box.minY, box2.minY) + 0.5625,
         Math.max(box.maxZ, box2.maxZ)
      );
   }

   public void handleBlockPos(BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Consumer<BlockHitResult> consumer) {
      BlockHitResult blockHitResult;
      if (this.getBooleanValueSetting7().getValue()) {
         Direction direction = this.resolveDirection3(blockPos, clientPlayerEntity, clientWorld);
         if (direction == null) {
            return;
         }

         Vec3d vec3d = new Vec3d(
            blockPos.getX() + 0.5 - direction.getOffsetX() * 0.5 + direction.getOffsetX(),
            blockPos.getY() + 0.5 - direction.getOffsetY() * 0.5 + direction.getOffsetY(),
            blockPos.getZ() + 0.5 - direction.getOffsetZ() * 0.5 + direction.getOffsetZ()
         );
         blockHitResult = new BlockHitResult(vec3d, direction, blockPos, false);
      } else {
         blockHitResult = new BlockHitResult(new Vec3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5), Direction.DOWN, blockPos, false);
      }

      consumer.accept(blockHitResult);
   }

   @Override
   public void run4() {
      this.floatValue = 0.0F;
      this.run2();
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!clientWorld.getDimension().bedWorks()) {
         Block block = itemSwitcher.resolveObject(
            slot -> slot.getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BedBlock ? blockItem.getBlock() : null
         );
         boolean flag = block != null;
         this.floatValueMutableObject.setValue(Float.MIN_VALUE);
         this.bedPositionMutableObject.setValue(null);

         for (AutoBedModule.BedPosition bedPosition : this.bedPositionBlockPosGrid.getObjectArray()) {
            if (bedPosition.isFlag3()
               && !(bedPosition.getFloatValue2() < this.minDamage.getValue())
               && (
                  this.suicide.getValue()
                     || !(bedPosition.getFloatValue() > this.maxSelfDamage.getValue())
                        && !(bedPosition.getFloatValue() >= EntityUtils.computeLivingEntity(clientPlayerEntity))
               )
               && bedPosition.getFloatValue2() > (Float)this.floatValueMutableObject.getValue()) {
               this.floatValueMutableObject.setValue(bedPosition.getFloatValue2());
               this.bedPositionMutableObject.setValue(bedPosition);
            }
         }

         AutoBedModule.BedPosition bedPosition2 = (AutoBedModule.BedPosition)this.bedPositionMutableObject.getValue();
         if (bedPosition2 != null) {
            Direction direction = bedPosition2.direction;
            if (bedPosition2.direction != null) {
               this.floatValue = bedPosition2.getFloatValue2();
               if (bedPosition2.isFlag()) {
                  if (this.normal3.testNumber2(this.placeDelay2.getValue())) {
                     this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, bedPosition2);
                  }
               } else if (flag && this.normal4.testNumber2(this.placeDelay.getValue())) {
                  this.resolvePlacement7(block, bedPosition2, direction);
                  if (this.getBooleanValueSetting8().getValue()) {
                     Box box = this.createBox(bedPosition2, direction);
                     Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getBox().equals(box));
                     Render3d.handleCuboid(
                        Render3d.Cuboid.createBuilder()
                           .resolveBuilder11(box)
                           .resolveBuilder18(this.getColorSetting2().getValue(), this.getColorSetting().getValue())
                           .resolveBuilder12(this.getFloatValueSetting().getValue())
                           .resolveBuilder5(this.getLongValueSetting().getValue())
                           .getCuboid()
                     );
                  }
               }
            }
         }
      }
   }

   public void handleSwitchResult(ItemSwitcher.SwitchResult switchResult, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      TickUtils.handleClientWorld(clientWorld, integer -> {
         boolean flag = clientPlayerEntity.isSneaking();
         if (flag) {
            clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.RELEASE_SHIFT_KEY));
         }

         this.handleBlockPos(blockPos, clientPlayerEntity, clientWorld, blockHitResult -> {
            clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(switchResult.hand(), blockHitResult, 0));
            clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(switchResult.hand()));
         });
         if (flag) {
            clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.PRESS_SHIFT_KEY));
         }
      });
   }

   @Override
   public String nothing() {
      return String.valueOf(MathUtils.computeFloat5(this.floatValue, 2));
   }

   @Override
   public boolean alwaysFalse() {
      return false;
   }

   public void handleBedPosition(AutoBedModule.BedPosition bedPosition, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (bedPosition.flag) {
         BlockPos blockPos = bedPosition.offset(bedPosition.direction);
         List<BlockPos> list = List.of(bedPosition, blockPos);
         bedPosition.setFloatValue(this.phobosClient.getDamageCalculator().computeEntity3(clientPlayerEntity, clientWorld, 5.0F, blockPos.toCenterPos(), list));

         for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
            if (playerEntity != null
               && !playerEntity.isDead()
               && !playerEntity.equals(clientPlayerEntity)
               && playerEntity.isAlive()
               && !(playerEntity.squaredDistanceTo(clientPlayerEntity) > MathHelper.square(324.0))
               && !this.phobosClient.getFriendList().testUUID(playerEntity.getUuid())
               && !(playerEntity.squaredDistanceTo(bedPosition.toCenterPos()) > 144.0)
               && !playerEntity.isCreative()
               && !playerEntity.isSpectator()) {
               float f = this.phobosClient.getDamageCalculator().computeEntity3(playerEntity, clientWorld, 5.0F, blockPos.toCenterPos(), list);
               if (f > bedPosition.getFloatValue2()) {
                  bedPosition.setFloatValue2(f);
                  bedPosition.setPlayerEntity(playerEntity);
               }
            }
         }
      } else {
         for (Direction direction : bedPosition.directionArray) {
            if (direction != null) {
               BlockPos blockPos2 = bedPosition.offset(direction);
               List<BlockPos> list2 = List.of(bedPosition, blockPos2);
               bedPosition.setFloatValue(
                  this.phobosClient.getDamageCalculator().computeEntity3(clientPlayerEntity, clientWorld, 5.0F, blockPos2.toCenterPos(), list2)
               );

               for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
                  if (playerEntity2 != null
                     && !playerEntity2.isDead()
                     && !playerEntity2.equals(clientPlayerEntity)
                     && playerEntity2.isAlive()
                     && !(playerEntity2.squaredDistanceTo(clientPlayerEntity) > MathHelper.square(324.0))
                     && !this.phobosClient.getFriendList().testUUID(playerEntity2.getUuid())
                     && !(playerEntity2.squaredDistanceTo(bedPosition.toCenterPos()) > 144.0)
                     && !playerEntity2.isCreative()
                     && !playerEntity2.isSpectator()) {
                     float f2 = this.phobosClient.getDamageCalculator().computeEntity3(playerEntity2, clientWorld, 5.0F, blockPos2.toCenterPos(), list2);
                     if (f2 > bedPosition.getFloatValue2()) {
                        bedPosition.setFloatValue2(f2);
                        bedPosition.setPlayerEntity(playerEntity2);
                        bedPosition.setDirection(direction);
                     }
                  }
               }
            }
         }
      }
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, AutoBedModule.BedPosition bedPosition) {
      if (clientWorld.getBlockState(bedPosition).isOf(Blocks.RESPAWN_ANCHOR) || bedPosition.flag) {
         this.phobosClient
            .getInventoryManager()
            .handleConsumer2(itemSwitcher -> this.handleItemSwitcher3(itemSwitcher, bedPosition, clientPlayerEntity, clientWorld));
      }
   }

   public AutoBedModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoBed", "Explodes beds on enemies in nether", Categories.combat, 6);
      this.maxSelfDamage = this.resolveSetting2(new NumberSetting<>("MaxSelfDamage", 8.0F, 0.0F, 36.0F));
      this.suicide = this.resolveSetting2(new BooleanSetting("Suicide").resolveSetting7("Ignore local player damage"));
      this.placeDelay = this.resolveSetting2(new LongNumberSetting("PlaceDelay", 300L, 0L, 500L, true));
      this.placeDelay2 = this.resolveSetting2(new LongNumberSetting("PlaceDelay", 0L, 0L, 500L, true));
      this.lockedExecutor = new LockedExecutor();
      this.bedPositionBlockPosGrid = new BlockPosGrid<>(7, AutoBedModule.BedPosition::new, new AutoBedModule.BedPosition[0]);
      this.normal4 = new TimeGate.Normal();
      this.normal3 = new TimeGate.Normal();
      this.floatValueMutableObject = new MutableObject(0.0F);
      this.bedPositionMutableObject = new MutableObject(null);
      this.floatValue = 0.0F;
      this.resolveSettingGroup2("Calculation", this.minDamage, this.maxSelfDamage, this.suicide);
      this.resolveSettingGroup2("Placement", this.placeDelay, this.placeDelay2);
      this.handleListener(
         new SafeListener<TickCounter>() {
            public void handleObject(
               TickCounter tickCounter,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               PlayerEntity playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient);
               if (playerEntity == null) {
                  AutoBedModule.this.run2();
               } else {
                  AutoBedModule.this.lockedExecutor
                     .handleRunnable(
                        () -> {
                           ClientWorld clientWorldx = phobosClient.getWorldHolder().getClientWorld();

                           for (AutoBedModule.BedPosition bedPosition : AutoBedModule.this.bedPositionBlockPosGrid.getObjectArray()) {
                              bedPosition.handleClientPlayerEntity(
                                 clientPlayerEntity,
                                 clientWorldx,
                                 (blockPos, direction) -> AutoBedModule.this.resolvePlacement9(Blocks.STONE, blockPos).isSuccessful()
                                    && !BlockUtils.testBlockPos7(blockPos.offset(direction), clientWorldx)
                              );
                              if (bedPosition.isFlag3()) {
                                 AutoBedModule.this.handleBedPosition(bedPosition, clientPlayerEntity, clientWorldx);
                              }
                           }
                        }
                     );
               }
            }
         }
      );
   }

   public void run2() {
      for (AutoBedModule.BedPosition bedPosition : this.bedPositionBlockPosGrid.getObjectArray()) {
         bedPosition.run();
      }
   }

   private static class BedPosition extends MutableBlockPos {
      public static final Logger LOGGER = LoggerFactory.getLogger(AutoBedModule.BedPosition.class);
      public float floatValue;
      public PlayerEntity playerEntity;
      public final Vec3i vec3i;
      public boolean flag;
      public BedPart bedPart;
      public Direction[] directionArray = new Direction[4];
      public boolean flag2;
      public boolean flag3;
      public float floatValue2 = 0.0F;
      public Direction direction;

      public Direction[] getDirectionArray() {
         return this.directionArray;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public boolean isFlag3() {
         return this.flag3;
      }

      public void setBedPart(BedPart bedPart) {
         this.bedPart = bedPart;
      }

      public void setDirection(Direction direction) {
         this.direction = direction;
      }

      public void run() {
         this.direction = null;
         Arrays.fill(this.directionArray, null);
         this.bedPart = null;
         this.floatValue2 = 0.0F;
         this.floatValue = Float.MAX_VALUE;
         this.flag3 = false;
         this.flag = false;
         this.flag2 = false;
         this.playerEntity = null;
      }

      public Direction getDirection() {
         return this.direction;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public BedPart getBedPart() {
         return this.bedPart;
      }

      public void setFlag3(boolean flag) {
         this.flag3 = flag;
      }

      public void setDirectionArray(Direction[] directions) {
         this.directionArray = directions;
      }

      public void setPlayerEntity(PlayerEntity playerEntity) {
         this.playerEntity = playerEntity;
      }

      public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BiPredicate<BlockPos, Direction> biPredicate) {
         this.run();
         this.set(
            Math.floor(clientPlayerEntity.getX()) + this.vec3i.getX(),
            Math.floor(clientPlayerEntity.getEyeY()) + this.vec3i.getY(),
            Math.floor(clientPlayerEntity.getZ()) + this.vec3i.getZ()
         );
         BlockState blockState2 = clientWorld.getBlockState(this);
         if (blockState2.getBlock().getBlastResistance() < 35.0F) {
            this.flag2 = true;
         }

         boolean flag = false;
         if (blockState2.getBlock() instanceof BedBlock) {
            this.flag = true;
            this.bedPart = (BedPart)blockState2.get(BedBlock.PART);
            this.direction = (Direction)blockState2.get(BedBlock.FACING);
         } else {
            if (!blockState2.isReplaceable()) {
               return;
            }

            int i = 0;

            for (Direction direction : DirectionUtils.directionArray) {
               BlockPos blockPos = this.offset(direction);
               BlockState blockState = clientWorld.getBlockState(blockPos);
               if (blockState.isReplaceable() && biPredicate.test(this, direction)) {
                  this.directionArray[i] = direction;
                  flag = true;
                  i++;
               }
            }
         }

         this.flag3 = this.flag || flag;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public PlayerEntity getPlayerEntity() {
         return this.playerEntity;
      }

      public Vec3i getVec3i() {
         return this.vec3i;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public BedPosition(Vec3i vec3i) {
         this.floatValue = Float.MAX_VALUE;
         this.vec3i = vec3i;
      }
   }
}
