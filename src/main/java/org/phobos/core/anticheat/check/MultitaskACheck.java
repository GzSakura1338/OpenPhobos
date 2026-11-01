package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class MultitaskACheck extends AntiCheatCheck {
   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMisc().getValue();
   }

   @Override
   public void run10() {
      if (this.antiCheatPlayer.flag11 && this.antiCheatPlayer.flag7) {
         this.handleText("Attacking while using");
      }
   }

   public MultitaskACheck(AntiCheatPlayer antiCheatPlayer) {
      super("MultitaskA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 2;
   }
}
