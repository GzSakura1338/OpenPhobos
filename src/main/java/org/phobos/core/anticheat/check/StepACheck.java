package org.phobos.core.anticheat.check;

import java.util.List;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;

public class StepACheck extends SimulationCheck {
   public final List<Double> doubleValueList = List.of(0.8, 1.0, 1.5, 2.0, 2.5, 3.0);

   @Override
   public void run3() {
      if (this.antiCheatPlayer.flag10 && this.antiCheatPlayer.flag6) {
         if (this.antiCheatPlayer.vec3d3.y > this.antiCheatPlayer.getVec3d6().y) {
            double d3 = this.antiCheatPlayer.vec3d3.y - this.antiCheatPlayer.getVec3d6().y;

            for (double d2 : this.doubleValueList) {
               double d = Math.abs(d2 - d3);
               if (d < 0.015) {
                  this.handleText(String.format("Distance: %.2f Inaccuracy: %.2f", d3, d));
               }
            }
         }
      }
   }

   public StepACheck(AntiCheatPlayer antiCheatPlayer) {
      super("StepA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 5;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }
}
