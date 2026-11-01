package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class AutoClickerACheck extends AntiCheatCheck {
   public long longValue;
   public long longValue2 = -1L;
   public long longValue3 = -1L;

   @Override
   public void run10() {
      this.longValue = this.longValue3;
      this.longValue3 = this.longValue2;
      this.longValue2 = System.currentTimeMillis();
   }

   @Override
   public void run3() {
      if (this.longValue3 != -1L && this.longValue2 != -1L) {
         long l = Math.abs(this.longValue3 - this.longValue2);
         long l2 = Math.abs(this.longValue - this.longValue3);
         if (Math.abs(l - l2) < 5L) {
            this.handleText("Delay: %s->%s & C".formatted(l2, l));
            this.longValue = -1L;
            this.longValue3 = -1L;
            this.longValue2 = -1L;
         }
      }
   }

   @Override
   public int value() {
      return 10;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getCombat().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getAutoClicker().getValue();
   }

   public AutoClickerACheck(AntiCheatPlayer antiCheatPlayer) {
      super("AutoClickerA", antiCheatPlayer);
      this.longValue = -1L;
   }
}
