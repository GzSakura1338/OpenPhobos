package org.phobos.core.anticheat.check;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.rotation.RotationUtils;

public class AimECheck extends AntiCheatCheck {
   public BlockPos blockPos = null;

   @Override
   public int value() {
      return 30;
   }

   @Override
   public void handleBlockPos(BlockPos blockPos, BlockState blockState) {
      this.blockPos = blockPos;
   }

   public AimECheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimE", antiCheatPlayer);
   }

   @Override
   public void run4() {
      if (this.blockPos != null) {
         float f = this.antiCheatPlayer.vec2f.y;
         float f2 = this.antiCheatPlayer.vec2f2.x;
         float f3 = this.antiCheatPlayer.vec2f.x;
         float f4 = this.antiCheatPlayer.vec2f2.x;

         for (Direction direction : Direction.values()) {
            float[] floats = RotationUtils.values4(this.antiCheatPlayer.abstractClientPlayerEntity, mc.world, this.blockPos, direction);
            boolean flag = Math.abs(floats[0] - f) < 0.2 || Math.abs(floats[0] - f2) < 0.2;
            boolean flag2 = Math.abs(floats[1] - f3) < 0.2 || Math.abs(floats[1] - f4) < 0.2;
            if (flag || flag2) {
               this.handleText("Yaw: %.2f Pitch: %.2f & ".formatted(floats[0], floats[1]) + (flag && flag2 ? "YP" : (flag ? "Y" : "P")));
               break;
            }
         }

         this.blockPos = null;
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getExperimental().getValue();
   }
}
