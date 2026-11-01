package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class AuraBCheck extends AntiCheatCheck {
   public int intValue = 0;

   @Override
   public void handleAntiCheatCheck2(AntiCheatCheck antiCheatCheck) {
      if (antiCheatCheck instanceof ReachACheck || antiCheatCheck instanceof AutoClickerACheck) {
         this.intValue++;
      }

      if (this.intValue > 10) {
         this.intValue = 0;
         this.run7();
      }
   }

   @Override
   public int value() {
      return 5;
   }

   public AuraBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("AuraB", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getCombat().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getAura().getValue();
   }
}
