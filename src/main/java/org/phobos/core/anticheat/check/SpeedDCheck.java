package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class SpeedDCheck extends SimulationCheck {
   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   public SpeedDCheck(AntiCheatPlayer antiCheatPlayer) {
      super("SpeedD", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 50;
   }

   @Override
   public void run3() {
      if (VectorMath.testVec3d(this.antiCheatPlayer.vec3d2) && VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
         this.run7();
      }
   }
}
