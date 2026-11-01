package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class FlyCCheck extends AntiCheatCheck {
   public FlyCCheck(AntiCheatPlayer antiCheatPlayer) {
      super("FlyC", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getFly().getValue();
   }

   @Override
   public int value() {
      return 10;
   }

   @Override
   public void run3() {
      if (this.antiCheatPlayer.abstractClientPlayerEntity.isSwimming() && !this.antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater()) {
         this.run7();
      }
   }
}
