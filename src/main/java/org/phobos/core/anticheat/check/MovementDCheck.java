package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;

public class MovementDCheck extends SimulationCheck {
   @Override
   public void run3() {
      if (!this.isFlag4()) {
         ;
      }
   }

   public MovementDCheck(AntiCheatPlayer antiCheatPlayer) {
      super("MovementD", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public int value() {
      return 15;
   }
}
