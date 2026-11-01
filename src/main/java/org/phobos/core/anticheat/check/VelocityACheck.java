package org.phobos.core.anticheat.check;

import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class VelocityACheck extends SimulationCheck {
   public int intValue;
   public int intValue4;
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public Vec3d vec3d;

   public void run2() {
      this.flag3 = true;
      this.intValue = 0;
      this.vec3d = Vec3d.ZERO;
   }

   public boolean testAntiCheatPlayer(AntiCheatPlayer antiCheatPlayer, VelocityACheck.VelocityDirection velocityDirection) {
      if (antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()) {
         return false;
      }

      if (antiCheatPlayer.abstractClientPlayerEntity.isOnFire()
         || antiCheatPlayer.abstractClientPlayerEntity.isInLava()
         || !antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
         return false;
      }

      if (antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater() && antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
         return false;
      }

      if (antiCheatPlayer.abstractClientPlayerEntity.isInsideWall()) {
         return false;
      }

      if (antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.WITHER)
         || antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.POISON)
         || antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.INSTANT_DAMAGE)) {
         return false;
      }

      assert mc.world != null;
      if (BlockPosUtils.resolveSet3(antiCheatPlayer.abstractClientPlayerEntity, antiCheatPlayer.abstractClientPlayerEntity.getBoundingBox().minY - 0.05)
         .stream()
         .anyMatch(blockPosx -> mc.world.getBlockState(blockPosx).isOf(Blocks.MAGMA_BLOCK))) {
         return false;
      }

      if (antiCheatPlayer.abstractClientPlayerEntity.getHungerManager().getFoodLevel() == 0 && mc.world.getDifficulty().equals(Difficulty.HARD)) {
         return false;
      }

      if (velocityDirection != null) {
         BlockPos blockPos = antiCheatPlayer.abstractClientPlayerEntity.getBlockPos();
         switch (velocityDirection) {
            case HORIZONTAL:
            case ALL:
               for (Direction direction : DirectionUtils.directionArray) {
                  if (!mc.world.getBlockState(blockPos.offset(direction)).isAir()) {
                     return false;
                  }
               }
               break;
            case VERTICAL:
               if (!mc.world.getBlockState(blockPos.up()).isAir()
                  || !mc.world.getBlockState(blockPos).isAir()
                  || !mc.world.getBlockState(blockPos.up()).isAir()) {
                  return false;
               }
         }
      }

      return true;
   }

   @Override
   public void run3() {
      if (this.testAntiCheatPlayer2(this.antiCheatPlayer)) {
         this.intValue4 = (int)(Math.ceil(this.antiCheatPlayer.intValue / 50.0) + 2.0);
      } else {
         if (this.intValue4 > 0) {
            this.intValue4--;
         }

         if (this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime > 0) {
            if (!this.flag) {
               this.run2();
               this.vec3d = this.antiCheatPlayer.vec3d3;
               this.flag2 = this.antiCheatPlayer.abstractClientPlayerEntity.isOnGround();
               this.flag = true;
               this.flag3 = this.intValue4 > 0;
               return;
            }
         } else {
            this.flag = false;
         }

         if (this.flag && !this.flag3) {
            if (this.testAntiCheatPlayer(this.antiCheatPlayer, VelocityACheck.VelocityDirection.ALL) && this.antiCheatPlayer.vec3d3.equals(this.vec3d)) {
               this.handleText2("& Full");
               return;
            }

            if (this.testAntiCheatPlayer(this.antiCheatPlayer, VelocityACheck.VelocityDirection.VERTICAL)
               && this.antiCheatPlayer.vec3d3.y <= this.vec3d.y
               && this.flag2
               && this.antiCheatPlayer.abstractClientPlayerEntity.isOnGround()) {
               this.handleText2("& Vertical");
               return;
            }

            if (this.testAntiCheatPlayer(this.antiCheatPlayer, VelocityACheck.VelocityDirection.HORIZONTAL)
               && VectorMath.createVec3d(this.antiCheatPlayer.vec3d3).distanceTo(VectorMath.createVec3d(this.vec3d)) == 0.0
               && this.flag2) {
               this.handleText2("& Horizontal");
               return;
            }

            this.run2();
         }
      }
   }

   public void handleText2(String text) {
      if (this.intValue > this.antiCheatPlayer.intValue + 20) {
         this.handleText("Latency: %d  Delayed: %d %s".formatted(this.antiCheatPlayer.intValue, this.intValue, text));
         this.run2();
      } else {
         this.intValue += 50;
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   public VelocityACheck(AntiCheatPlayer antiCheatPlayer) {
      super("VelocityA", antiCheatPlayer);
   }

   public boolean testAntiCheatPlayer2(AntiCheatPlayer antiCheatPlayer) {
      return antiCheatPlayer.floatValue > 3.0F && !antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.SLOW_FALLING);
   }

   @Override
   public int value() {
      return 2;
   }

   private enum VelocityDirection {
      HORIZONTAL,
      VERTICAL,
      ALL;
   }
}
