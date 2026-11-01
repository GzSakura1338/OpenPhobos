package org.phobos.core.anticheat.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class ImprobableCheck extends AntiCheatCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(ImprobableCheck.class);
   public int intValue = 0;

   @Override
   public int value() {
      return 0;
   }

   @Override
   public void handleAntiCheatCheck2(AntiCheatCheck antiCheatCheck) {
      if (!(antiCheatCheck instanceof ImprobableCheck)) {
         if (antiCheatCheck instanceof AutoClickerACheck
            || antiCheatCheck instanceof VelocityACheck
            || antiCheatCheck instanceof PlaceACheck
            || antiCheatCheck instanceof ReachACheck
            || antiCheatCheck instanceof AuraACheck
            || antiCheatCheck instanceof AuraBCheck) {
            this.intValue++;
         }

         if (this.intValue > 10) {
            this.intValue = 0;
            this.run7();
         }
      }
   }

   @Override
   public boolean isFlag4() {
      return false;
   }

   public ImprobableCheck(AntiCheatPlayer antiCheatPlayer) {
      super("Improbable", antiCheatPlayer);
   }
}
