package org.phobos.core.anticheat.check;

import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class InvalidYawCheck extends AntiCheatCheck {
   public InvalidYawCheck(AntiCheatPlayer antiCheatPlayer) {
      super("InvalidYaw", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 1;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }

   @Override
   public void run3() {
      if (Math.abs(this.antiCheatPlayer.abstractClientPlayerEntity.getYaw()) > 360000.0F) {
         this.handleText("Yaw: %.1f°".formatted(this.antiCheatPlayer.abstractClientPlayerEntity.getYaw()));
         if (this.antiCheatPlayer.getAntiCheatModule().getExperimental().getValue()) {
            this.antiCheatPlayer.abstractClientPlayerEntity.setYaw(this.antiCheatPlayer.abstractClientPlayerEntity.getYaw() % 180.0F);
         }
      }
   }
}
