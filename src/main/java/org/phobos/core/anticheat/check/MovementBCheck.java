package org.phobos.core.anticheat.check;

import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class MovementBCheck extends SimulationCheck {
   public final MovingFakePlayer movingFakePlayer;

   public MovementBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("MovementB", antiCheatPlayer);
      this.movingFakePlayer = new MovingFakePlayer(antiCheatPlayer.abstractClientPlayerEntity.clientWorld);
   }

   @Override
   public int value() {
      return 10;
   }

   @Override
   public boolean isFlag4() {
      return true;
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!this.antiCheatPlayer.flag10
            && !MovementACheck.registryEntryList
               .stream()
               .anyMatch(registryEntry -> this.antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(registryEntry))
            && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isInLava()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isClimbing()
            && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0
            && !this.antiCheatPlayer
               .abstractClientPlayerEntity
               .clientWorld
               .getBlockCollisions(
                  this.antiCheatPlayer.abstractClientPlayerEntity, this.antiCheatPlayer.abstractClientPlayerEntity.getBoundingBox().offset(0.0, -0.75, 0.0)
               )
               .iterator()
               .hasNext()) {
            this.movingFakePlayer.copyFrom(this.antiCheatPlayer.abstractClientPlayerEntity);
            this.movingFakePlayer.setPosition(this.antiCheatPlayer.vec3d3.x, this.antiCheatPlayer.vec3d3.y, this.antiCheatPlayer.vec3d3.z);
            this.movingFakePlayer.setVelocity(this.antiCheatPlayer.vec3d2);
            Vec3d vec3d2 = this.movingFakePlayer.adjustMovementForSneaking(this.movingFakePlayer.getVelocity(), MovementType.SELF);
            Vec3d vec3d3 = this.movingFakePlayer.resolveVec3d(vec3d2);
            int i2 = 0;

            for (int i = 1; i < 3; i++) {
               double d = VectorMath.computeDouble(vec3d3.y, i);
               Vec3d vec3d = new Vec3d(vec3d3.x, d + 0.08, vec3d3.z);
               if (Math.abs(this.antiCheatPlayer.vec3d2.y - vec3d.y) > 0.01) {
                  i2++;
               }
            }

            if (i2 >= 2) {
               this.handleText("Invalid Y Velocity");
            }
         }
      }
   }
}
