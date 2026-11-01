package org.phobos.core.anticheat.check;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.rotation.RotationUtils;

public class ScaffoldACheck extends AntiCheatCheck {
   @Override
   public void handleBlockPos(BlockPos blockPos, BlockState blockState) {
      if (!this.isFlag4()) {
         Entity entity = this.resolveList2(7.0).stream().filter(entityx -> entityx.isAlive() && !entityx.isSpectator()).findFirst().orElse(null);
         if (entity == null) {
            if (!this.antiCheatPlayer
               .abstractClientPlayerEntity
               .clientWorld
               .getBlockCollisions(
                  this.antiCheatPlayer.abstractClientPlayerEntity,
                  this.antiCheatPlayer.abstractClientPlayerEntity.getBoundingBox().expand(-0.3F, 0.0, -0.3F).offset(0.0, -0.5, 0.0)
               )
               .iterator()
               .hasNext()) {
               if (BlockPosUtils.resolveSet3(this.antiCheatPlayer.abstractClientPlayerEntity, this.antiCheatPlayer.abstractClientPlayerEntity.getBoundingBox().minY - 0.5)
                  .contains(blockPos)) {
                  float[] floats = RotationUtils.values2(this.antiCheatPlayer.abstractClientPlayerEntity, blockPos.toCenterPos());
                  double d = Math.abs(floats[0] - this.antiCheatPlayer.vec2f.y);
                  double d2 = Math.abs(floats[1] - this.antiCheatPlayer.vec2f.x);
                  double d3 = Math.abs(floats[0] - this.antiCheatPlayer.vec2f2.y);
                  double d4 = Math.abs(floats[1] - this.antiCheatPlayer.vec2f2.x);
                  boolean flag = d > 45.0 && d3 > 45.0;
                  boolean flag2 = d2 > 45.0 && d4 > 45.0;
                  if (flag || flag2) {
                     this.run7();
                  }
               }
            }
         }
      }
   }

   public ScaffoldACheck(AntiCheatPlayer antiCheatPlayer) {
      super("ScaffoldA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 8;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getScaffold().getValue();
   }
}
