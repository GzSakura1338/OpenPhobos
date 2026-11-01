package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class InvalidPitchCheck extends AntiCheatCheck {
   @Override
   public int value() {
      return 1;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }

   public InvalidPitchCheck(AntiCheatPlayer antiCheatPlayer) {
      super("InvalidPitch", antiCheatPlayer);
   }

   @Override
   public void run3() {
      if (this.antiCheatPlayer.vec2f.x > 90.0F || this.antiCheatPlayer.vec2f.x < -90.0F) {
         this.handleText("Pitch: %.2f".formatted(this.antiCheatPlayer.vec2f.x));
      }
   }
}
