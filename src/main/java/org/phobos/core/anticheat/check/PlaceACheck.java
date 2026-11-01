package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class PlaceACheck extends AntiCheatCheck {
   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getPlace().getValue();
   }

   @Override
   public int value() {
      return 2;
   }

   public PlaceACheck(AntiCheatPlayer antiCheatPlayer) {
      super("PlaceA", antiCheatPlayer);
   }

   @Override
   public void run4() {
      if (!this.antiCheatPlayer.flag2) {
         this.run7();
      }
   }
}
