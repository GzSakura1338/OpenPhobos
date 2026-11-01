package org.phobos.core.anticheat.check;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class StrafeACheck extends SimulationCheck {
   public Set<Vec3d> vec3dSet;
   public final Set<Integer> integerSet = Set.of(1, 0, -1);

   @Override
   public int value() {
      return 20;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   public StrafeACheck(AntiCheatPlayer antiCheatPlayer) {
      super("StrafeA", antiCheatPlayer);
      this.vec3dSet = null;
   }

   @Override
   public void run3() {
      if (!this.antiCheatPlayer.flag10
         && VectorMath.testVec3d(this.antiCheatPlayer.vec3d2)
         && !(Math.abs(this.antiCheatPlayer.vec2f2.y - this.antiCheatPlayer.vec2f.y) < 5.0F)
         && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
         && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()) {
         if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
            if (!this.antiCheatPlayer.booleanValueList.subList(0, 10).stream().anyMatch(booleanValue -> booleanValue != this.antiCheatPlayer.flag)) {
               if (this.vec3dSet != null) {
                  for (Vec3d vec3d : this.vec3dSet) {
                     double d = VectorMath.computeVec3d2(this.antiCheatPlayer.vec3d2, vec3d);
                     if (d <= 0.005) {
                        this.handleText(String.format("Divisor: %.4f", d));
                     }
                  }
               }

               this.vec3dSet = new HashSet<>();

               for (int i2 : this.integerSet) {
                  for (int i : this.integerSet) {
                     Vec3d vec3d2 = VectorMath.createVec3d2(
                        VectorMath.computePlayerEntity(this.antiCheatPlayer.abstractClientPlayerEntity),
                        VectorMath.computeFloat(
                           i2,
                           i,
                           this.antiCheatPlayer.abstractClientPlayerEntity.prevYaw
                              + (this.antiCheatPlayer.abstractClientPlayerEntity.getYaw() - this.antiCheatPlayer.abstractClientPlayerEntity.prevYaw) * 50.0F
                        ),
                        this.antiCheatPlayer.vec3d2.y
                     );
                     this.vec3dSet.add(vec3d2);
                  }
               }
            }
         }
      } else {
         this.vec3dSet = null;
      }
   }
}
