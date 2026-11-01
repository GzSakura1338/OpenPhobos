package org.phobos.module.movement;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class FastFallModule extends Module {
   public final Setting<Double> speed;
   public final Setting<Double> height;
   public final Setting<FastFallModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", FastFallModule.Mode.Normal));
   public boolean flag;
   public final Setting<Boolean> verify = this.resolveSetting2(new BooleanSetting("Verify").resolveSetting7("Tests if you will fall correctly or not"));
   public boolean flag2;
   public final Setting<Boolean> correction;

   public boolean testPlayerEntity(PlayerEntity playerEntity, Vec3d vec3d, ClientWorld clientWorld) {
      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
      movingFakePlayer.copyFrom(playerEntity);
      movingFakePlayer.setVelocity(vec3d);
      Vec3d vec3d2 = movingFakePlayer.resolveVec3d(vec3d = movingFakePlayer.adjustMovementForSneaking(movingFakePlayer.getVelocity(), MovementType.SELF));
      return vec3d.y != vec3d2.y && vec3d.y < 0.0;
   }

   public double computePlayerEntity(PlayerEntity playerEntity, World world) {
      double d2 = playerEntity.getY();
      double d3 = this.height.getValue();

      for (double d = d2; d > d2 - d3; d -= 0.001) {
         BlockPos blockPos = BlockPos.ofFloored(playerEntity.getX(), d, playerEntity.getZ());
         BlockState blockState = world.getBlockState(blockPos);
         if (!blockState.getCollisionShape(world, blockPos).isEmpty()) {
            if (blockState.getBlock() instanceof SlabBlock) {
               return -1.0;
            }

            return d;
         }
      }

      return -1.0;
   }

   public double value() {
      return this.verify.getValue() ? 0.01 : 1.0;
   }

   public boolean testPlayerEntity2(PlayerEntity playerEntity, Vec3d vec3d, ClientWorld clientWorld, boolean flag) {
      double d;
      return !flag
         && !MovementModel.testPlayerEntity(playerEntity)
         && playerEntity.fallDistance < 0.5
         && playerEntity.getVelocity().y < 0.0
         && playerEntity.isOnGround()
         && (
            !this.verify.getValue()
               || playerEntity.isOnGround()
                  && !this.testPlayerEntity(playerEntity, vec3d, clientWorld)
                  && playerEntity.getY() - (d = this.computePlayerEntity(playerEntity, clientWorld)) > 0.625
                  && playerEntity.getY() - d <= this.height.getValue()
         );
   }

   public Vec3d createVec3d(Vec3d vec3d) {
      return new Vec3d(vec3d.x * this.value(), Math.min(-this.speed.getValue(), vec3d.y), vec3d.z * this.value());
   }

   public FastFallModule(PhobosClient phobosClient) {
      super(phobosClient, "FastFall", Categories.movement, "Makes you fall fast");
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 6.0, 0.5, 150.0));
      this.correction = this.resolveSetting2(
         new BooleanSetting("Correction").resolveSetting4(this.mode, FastFallModule.Mode.Strict).resolveSetting7("Corrects horizontal velocity")
      );
      this.speed = this.resolveSetting2(new NumberSetting<>("Speed", 4.0, 1.0, 10.0));
      this.resolveSettingGroup(this.verify, this.height);
      this.handleListener(
         new SafeListener<MoveEvent2>(-1000) {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SpeedModule speedModule = SpeedModule.getSpeedModule();
               if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
                  switch ((FastFallModule.Mode)FastFallModule.this.mode.getValue()) {
                     case Normal:
                        if (FastFallModule.this.testPlayerEntity2(
                           clientPlayerEntity, moveEvent2.getVec3d(), clientWorld, speedModule != null && speedModule.check7()
                        )) {
                           moveEvent2.setVec3d(FastFallModule.this.createVec3d(moveEvent2.getVec3d()));
                        }
                        break;
                     case Strict:
                        if (SpeedModule.getSpeedModule().check7() || LongJumpModule.getLongJumpModule().check7()) {
                           return;
                        }

                        if (!clientPlayerEntity.isOnGround()) {
                           if (FastFallModule.this.correction.getValue()) {
                              PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, 0.0);
                              PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, 0.0);
                           }

                           if (clientPlayerEntity.getVelocity().getY() < 0.0 && FastFallModule.this.flag2) {
                              phobosClient.getTimerManager().handleFloat(2.5F, TimerPriority.High);
                              FastFallModule.this.flag = true;
                              return;
                           }

                           FastFallModule.this.flag2 = false;
                        } else {
                           PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -0.08);
                           FastFallModule.this.flag2 = true;
                        }

                        if (FastFallModule.this.flag) {
                           phobosClient.getTimerManager().run2();
                        }
                  }
               }
            }
         }
      );
   }

   private enum Mode {
      Normal,
      Strict;
   }
}
