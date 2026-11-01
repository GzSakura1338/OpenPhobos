package org.phobos.core.anticheat.check;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.rotation.RotationUtils;

public class PlaceBCheck extends AntiCheatCheck {
   public BlockPos blockPos = null;

   public PlaceBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("PlaceB", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 2;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getPlace().getValue();
   }

   @Override
   public void handleBlockPos(BlockPos blockPos, BlockState blockState) {
      this.blockPos = blockPos;
   }

   @Override
   public void run4() {
      if (this.blockPos != null) {
         float[] floats = RotationUtils.values2(this.antiCheatPlayer.abstractClientPlayerEntity, this.blockPos.toCenterPos());
         double d = Math.abs(floats[0] - this.antiCheatPlayer.vec2f.y);
         double d2 = Math.abs(floats[1] - this.antiCheatPlayer.vec2f.x);
         double d3 = Math.abs(floats[0] - this.antiCheatPlayer.vec2f2.y);
         double d4 = Math.abs(floats[1] - this.antiCheatPlayer.vec2f2.x);
         boolean flag = d > 45.0 && d3 > 45.0;
         boolean flag2 = d2 > 45.0 && d4 > 45.0;
         if (flag || flag2) {
            this.handleText("Yaw: %.2f Pitch: %.2f Delta: %.2f, %.2f".formatted(floats[0], floats[1], d, d2));
         }

         this.blockPos = null;
      }
   }
}
