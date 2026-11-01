package org.phobos.core.anticheat.check;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.math.VectorMath;

public class ScaffoldBCheck extends AntiCheatCheck {
   @Override
   public int value() {
      return 10;
   }

   public ScaffoldBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("ScaffoldB", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getScaffold().getValue();
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isClimbing()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isInLava()) {
            if (!this.antiCheatPlayer.flag10) {
               List<Vec3d> list = VectorMath.resolveList(this.antiCheatPlayer.vec3dList);
               double d = 0.0;
               double d2 = 0.0;
               int i = 0;

               for (Vec3d vec3d : list) {
                  if (vec3d.y != 0.0) {
                     if (vec3d.y == d && vec3d.y == d2) {
                        i++;
                     } else if (d == vec3d.y) {
                        d2 = vec3d.y;
                     } else {
                        d = vec3d.y;
                     }
                  }
               }

               if (i >= 8) {
                  this.run7();
               }
            }
         }
      }
   }
}
