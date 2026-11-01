package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class BlinkACheck extends AntiCheatCheck {
   @Override
   public int value() {
      return 10;
   }

   @Override
   public void run3() {
      if (this.antiCheatPlayer.vec3d6 != null && !this.antiCheatPlayer.flag9) {
         if (this.antiCheatPlayer.vec3d6.distanceTo(this.antiCheatPlayer.vec3d3)
            > 8.0 * this.antiCheatPlayer.doubleValue
               + this.antiCheatPlayer.abstractClientPlayerEntity.fallDistance
               + this.antiCheatPlayer.getAntiCheatModule().getThreshold().getValue()) {
            this.run7();
         }
      }
   }

   public BlinkACheck(AntiCheatPlayer antiCheatPlayer) {
      super("BlinkA", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue();
   }
}
