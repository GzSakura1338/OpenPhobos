package org.phobos.core.anticheat.check;

import java.util.Set;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class AimCCheck extends AntiCheatCheck {
   public final Set<Float> floatValueSet = Set.of(180.0F, 90.0F, -90.0F, 135.0F, -135.0F, 0.0F, 45.0F, -45.0F);

   @Override
   public void run3() {
      if (this.antiCheatPlayer.abstractClientPlayerEntity.age >= 20) {
         float f = this.antiCheatPlayer.vec2f.y - this.antiCheatPlayer.vec2f2.y;
         float f2 = this.antiCheatPlayer.vec2f.x - this.antiCheatPlayer.vec2f2.x;
         if (!this.testFloat(this.antiCheatPlayer.vec2f.y, this.antiCheatPlayer.vec2f2.y)) {
            if (this.testFloat(this.antiCheatPlayer.vec2f.x, 85.0F)
               && this.floatValueSet.stream().anyMatch(floatValue -> this.testFloat(floatValue, this.antiCheatPlayer.vec2f.y))) {
               if (this.floatValueSet.stream().anyMatch(floatValue -> this.testFloat(floatValue, this.antiCheatPlayer.vec2f2.y))) {
                  this.handleText("Scaffold-like rotation. Type: RotateWithMovement");
               } else {
                  if (f < 25.0F || f2 < 25.0F) {
                     return;
                  }

                  this.handleText("Scaffold-like rotation. Type: StartScaffolding");
               }
            }
         }
      }
   }

   public AimCCheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimC", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }

   @Override
   public int value() {
      return 1;
   }

   public boolean testFloat(float f, float f2) {
      return Math.abs(f - f2) < 0.05;
   }
}
