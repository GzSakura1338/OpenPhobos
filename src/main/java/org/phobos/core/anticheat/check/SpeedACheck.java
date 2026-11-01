package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class SpeedACheck extends SimulationCheck {
   public short shortValue;
   public boolean flag;

   @Override
   public void run5() {
      this.flag = true;
   }

   @Override
   public int value() {
      return 30;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public void run3() {
      if (this.flag && !this.antiCheatPlayer.flag8) {
         this.flag = false;
         this.shortValue = 10;
      } else {
         if (this.shortValue > 0) {
            this.shortValue--;
         }

         if (this.antiCheatPlayer.vec3d6 != null
            && !this.antiCheatPlayer.flag9
            && this.antiCheatPlayer.abstractClientPlayerEntity.isOnGround()
            && this.antiCheatPlayer.flag6
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater()) {
            double d;
            if (this.shortValue > 0) {
               d = 7.4;
            } else if (this.antiCheatPlayer.abstractClientPlayerEntity.isSprinting()) {
               d = 5.612;
            } else if (this.antiCheatPlayer.abstractClientPlayerEntity.isSilent()) {
               d = 1.295;
            } else {
               d = 4.317;
            }

            double d5 = VectorMath.computeVec3d(this.antiCheatPlayer.vec3d6, this.antiCheatPlayer.vec3d3);
            double d6 = d * this.antiCheatPlayer.doubleValue + this.antiCheatPlayer.getAntiCheatModule().getThreshold().getValue();
            if (d5 > d6) {
               this.handleText(String.format("Current: %.2f  Max: %.2f", d5, d6));
            }
         }
      }
   }

   public SpeedACheck(AntiCheatPlayer antiCheatPlayer) {
      super("SpeedA", antiCheatPlayer);
   }
}
