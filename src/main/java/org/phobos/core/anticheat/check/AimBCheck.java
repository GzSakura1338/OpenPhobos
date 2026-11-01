package org.phobos.core.anticheat.check;

import java.util.Set;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class AimBCheck extends AntiCheatCheck {
   public final Set<Integer> integerSet;
   public final Set<Integer> integerSet2 = Set.of(90, 135, 180);

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }

   @Override
   public int value() {
      return 10;
   }

   public AimBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimB", antiCheatPlayer);
      this.integerSet = Set.of(90, 135);
   }

   @Override
   public void run3() {
      if (Math.abs(this.antiCheatPlayer.vec2f.x) != 90.0F) {
         boolean flag = false;
         boolean flag2 = false;
         float f = 0.0F;
         float f2 = 0.0F;

         for (int i : this.integerSet) {
            if (Math.abs(Math.abs(this.antiCheatPlayer.vec2f2.x - this.antiCheatPlayer.vec2f.x) - i) < 1.0F) {
               flag = true;
               f = this.antiCheatPlayer.vec2f2.x - this.antiCheatPlayer.vec2f.x;
               break;
            }
         }

         for (int i2 : this.integerSet2) {
            if (Math.abs(Math.abs(this.antiCheatPlayer.vec2f2.y - this.antiCheatPlayer.vec2f.y) - i2) < 1.0F) {
               flag2 = true;
               f2 = this.antiCheatPlayer.vec2f2.y - this.antiCheatPlayer.vec2f.y;
               break;
            }
         }

         if (flag && flag2) {
            this.handleText(String.format("Divisor: %.1f, %.1f & YP", f2, f));
         } else if (flag) {
            this.handleText(String.format("Divisor: %.1f & P", f));
         } else if (flag2) {
            this.handleText(String.format("Divisor: %.1f & Y", f2));
         }
      }
   }
}
