package org.phobos.core.anticheat.check;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class SpeedCCheck extends SimulationCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(SpeedCCheck.class);

   public SpeedCCheck(AntiCheatPlayer antiCheatPlayer) {
      super("SpeedC", antiCheatPlayer);
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)
            && VectorMath.testVec3d(this.antiCheatPlayer.vec3d2)
            && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()) {
            Vec2f vec2f = this.antiCheatPlayer.vec2f;
            Vec2f vec2f2 = this.antiCheatPlayer.vec2f2;
            if (!(Math.abs(vec2f.y - vec2f2.y) > 15.0F)) {
               Vec3d vec3d = this.antiCheatPlayer.vec3d3.add(this.antiCheatPlayer.vec3d2.multiply(2.0));
               float[] floats = RotationUtils.values2(this.antiCheatPlayer.abstractClientPlayerEntity, vec3d);
               float f = floats[0];
               double d = VectorMath.computeVec3d3(this.antiCheatPlayer.vec3d6, this.antiCheatPlayer.vec3d3);
               if (Math.abs(f - vec2f.y) > 90.0F && d > 0.29 * this.antiCheatPlayer.doubleValue) {
                  this.handleText("Distance: %.2f".formatted(d));
               }
            }
         }
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public int value() {
      return 0;
   }
}
