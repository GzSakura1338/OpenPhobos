package org.phobos.core.anticheat.check;

import java.util.List;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.math.VectorMath;

public class NoSlowACheck extends AntiCheatCheck {
   public int intValue;
   public int intValue3;
   public final List<Double> doubleValueList = List.of(2.56, 1.92, 1.6, 1.4, 1.36, 1.26, 1.18, 1.16);

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   public NoSlowACheck(AntiCheatPlayer antiCheatPlayer) {
      super("NoSlowA", antiCheatPlayer);
      this.intValue3 = 0;
      this.intValue = 0;
   }

   @Override
   public void run3() {
      if (!this.antiCheatPlayer.abstractClientPlayerEntity.isUsingItem() || !this.antiCheatPlayer.flag7) {
         this.intValue3 = 0;
      } else if (this.antiCheatPlayer.flag8) {
         this.intValue = 4;
      } else if (this.intValue > 0) {
         this.intValue--;
      } else if (!this.antiCheatPlayer.abstractClientPlayerEntity.isSilent()) {
         double d = VectorMath.computeVec3d(this.antiCheatPlayer.vec3d6, this.antiCheatPlayer.vec3d3);
         double d2 = this.doubleValueList.get(this.intValue3) * this.antiCheatPlayer.doubleValue
            + this.antiCheatPlayer.getAntiCheatModule().getThreshold().getValue();
         if (d > d2) {
            this.handleText(String.format("Current: %.2f Max: %.2f", d, d2));
         }

         if (this.intValue3 < this.doubleValueList.size() - 1) {
            this.intValue3++;
         }
      }
   }

   @Override
   public int value() {
      return 20;
   }
}
