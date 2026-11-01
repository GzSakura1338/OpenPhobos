package org.phobos.core.anticheat.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.math.VectorMath;

public class FlyACheck extends AntiCheatCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(FlyACheck.class);

   @Override
   public int value() {
      return 15;
   }

   public FlyACheck(AntiCheatPlayer antiCheatPlayer) {
      super("FlyA", antiCheatPlayer);
   }

   @Override
   public void run3() {
      if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
         if (VectorMath.testVec3d(this.antiCheatPlayer.vec3d2)
            && (!this.antiCheatPlayer.flag10 || !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying())
            && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()) {
            if (this.antiCheatPlayer.vec3d.y == 0.0 && this.antiCheatPlayer.vec3d2.y == 0.0) {
               this.handleText(String.format("Invalid Y-Velocity: %.2f | Ground=%s", this.antiCheatPlayer.vec3d2.y, false));
            }
         }
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getFly().getValue();
   }
}
