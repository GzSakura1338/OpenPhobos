package org.phobos.core.anticheat.check;

import net.minecraft.entity.Entity;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.rotation.RotationUtils;

public class AimDCheck extends AntiCheatCheck {
   public boolean flag = false;

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getExperimental().getValue();
   }

   @Override
   public int value() {
      return 30;
   }

   @Override
   public void run3() {
      if (!this.isFlag4() && !this.flag) {
         float f2 = this.antiCheatPlayer.vec2f.y;
         float f3 = this.antiCheatPlayer.vec2f2.y;
         if (Math.abs(f2 - f3) > 15.0F) {
            for (Entity entity : this.resolveList2(6.0)) {
               float[] floats = RotationUtils.values(this.antiCheatPlayer.abstractClientPlayerEntity, entity);
               float f = floats[0];
               if (Math.abs(f2 - f) < 3.0F) {
                  this.handleText("Delta: %.2f".formatted(Math.abs(f2 - f)));
                  this.flag = true;
                  break;
               }

               if (Math.abs(f3 - f) < 3.0F) {
                  this.handleText("Delta: %.2f".formatted(Math.abs(f3 - f)));
                  this.flag = true;
                  break;
               }
            }
         }
      } else {
         this.flag = false;
      }
   }

   public AimDCheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimD", antiCheatPlayer);
   }
}
