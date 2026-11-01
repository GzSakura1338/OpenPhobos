package org.phobos.core.anticheat.check;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class StrafeBCheck extends SimulationCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(StrafeBCheck.class);

   @Override
   public int value() {
      return 20;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   public StrafeBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("StrafeB", antiCheatPlayer);
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)
            && VectorMath.testVec3d(this.antiCheatPlayer.vec3d2)
            && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0
            && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
            && !this.antiCheatPlayer.abstractClientPlayerEntity.getAbilities().flying) {
            if (!(Math.abs(this.antiCheatPlayer.vec2f.y - this.antiCheatPlayer.vec2f2.y) > 5.0F)) {
               Vec3d vec3d = this.antiCheatPlayer.vec3d3.add(this.antiCheatPlayer.vec3d2.multiply(2.0));
               float[] floats = RotationUtils.values2(this.antiCheatPlayer.abstractClientPlayerEntity, vec3d);
               float f = floats[0];
               if (Math.abs(f - this.antiCheatPlayer.vec2f.y) > 50.0F) {
                  double d = VectorMath.computeVec3d3(this.antiCheatPlayer.vec3d6, this.antiCheatPlayer.vec3d3);
                  if (d > 0.33848882987) {
                     this.handleText(
                        "Tick: %.2f Move: %.2f Yaw: %.2f".formatted(d, MathHelper.wrapDegrees(f), MathHelper.wrapDegrees(this.antiCheatPlayer.vec2f.y))
                     );
                  }
               }
            }
         }
      }
   }
}
