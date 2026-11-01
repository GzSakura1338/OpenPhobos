package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class HeuristicACheck extends AntiCheatCheck {
   public HeuristicACheck(AntiCheatPlayer antiCheatPlayer) {
      super("HeuristicA", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return false;
   }

   @Override
   public int value() {
      return 0;
   }
}
