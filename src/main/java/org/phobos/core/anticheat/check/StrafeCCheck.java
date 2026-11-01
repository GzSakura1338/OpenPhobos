package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class StrafeCCheck extends SimulationCheck {
   @Override
   public void run3() {
      if (!this.isFlag4()
         && !this.antiCheatPlayer.abstractClientPlayerEntity.getAbilities().flying
         && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
         && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0) {
         int i = this.antiCheatPlayer.intValue4;
         if (i >= 3) {
            double d = this.antiCheatPlayer.vec3d2.x;
            double d2 = this.antiCheatPlayer.vec3d2.z;
            double d3 = this.antiCheatPlayer.vec3d.x;
            double d4 = this.antiCheatPlayer.vec3d.z;
            double d5 = d3 * 0.91;
            double d6 = d4 * 0.91;
            double d7 = Math.abs(d - d5);
            double d8 = Math.abs(d2 - d6);
            boolean flag = (d7 > 0.031 || d8 > 0.031) && VectorMath.computePlayerEntity(this.antiCheatPlayer.abstractClientPlayerEntity) > 0.3;
            if (flag) {
               this.handleText("Offset: %.2f, %.2f".formatted(d7, d8));
            }
         }
      }
   }

   @Override
   public int value() {
      return 5;
   }

   @Override
   public boolean isFlag4() {
      return true;
   }

   public StrafeCCheck(AntiCheatPlayer antiCheatPlayer) {
      super("StrafeC", antiCheatPlayer);
   }
}
