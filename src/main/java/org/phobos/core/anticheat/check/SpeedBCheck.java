package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;

public class SpeedBCheck extends SimulationCheck {
   public SpeedBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("SpeedB", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public void run3() {
      if (this.antiCheatPlayer.abstractClientPlayerEntity.isSprinting() && this.antiCheatPlayer.abstractClientPlayerEntity.getHungerManager().getFoodLevel() <= 6) {
         this.run7();
      }
   }

   @Override
   public int value() {
      return 10;
   }
}
