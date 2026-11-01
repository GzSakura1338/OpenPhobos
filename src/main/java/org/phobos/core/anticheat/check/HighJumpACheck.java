package org.phobos.core.anticheat.check;

import net.minecraft.util.math.Vec3d;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class HighJumpACheck extends SimulationCheck {
   public double doubleValue = 0.0;
   public boolean flag = false;

   public HighJumpACheck(AntiCheatPlayer antiCheatPlayer) {
      super("HighJumpA", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public int value() {
      return 5;
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()) {
            if (this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()) {
               this.flag = true;
            } else {
               if (this.antiCheatPlayer.isFlag8()
                  && this.antiCheatPlayer.vec3d8 != this.antiCheatPlayer.vec3d7
                  && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0
                  && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()) {
                  if (this.antiCheatPlayer.vec3d3.y > this.doubleValue) {
                     this.doubleValue = this.antiCheatPlayer.vec3d3.y;
                  }

                  Vec3d vec3d = new Vec3d(0.0, this.antiCheatPlayer.vec3d8.y, 0.0);
                  Vec3d vec3d2 = new Vec3d(0.0, this.doubleValue, 0.0);
                  double d = vec3d2.distanceTo(vec3d);
                  double d2 = 1.0
                     + VectorMath.computeAbstractClientPlayerEntity(this.antiCheatPlayer.abstractClientPlayerEntity)
                     + this.antiCheatPlayer.getAntiCheatModule().getThreshold().getValue();
                  if (!this.flag && d > d2) {
                     this.handleText(String.format("Current: %.2f Max: %.2f", d, d2));
                     this.flag = true;
                  }
               } else {
                  this.flag = false;
                  this.doubleValue = 0.0;
               }
            }
         }
      }
   }
}
