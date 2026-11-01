package org.phobos.core.anticheat.check;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.math.VectorMath;

public class FlyBCheck extends AntiCheatCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(FlyBCheck.class);
   public final List<Block> blockList = List.of(Blocks.COBWEB, Blocks.WATER, Blocks.LAVA, Blocks.SLIME_BLOCK, Blocks.SOUL_SAND);

   @Override
   public int value() {
      return 30;
   }

   public FlyBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("FlyB", antiCheatPlayer);
   }

   @Override
   public void run3() {
      if (!this.antiCheatPlayer.flag10
         && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()
         && !this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()
         && !this.blockList.stream().anyMatch(block -> this.antiCheatPlayer.abstractClientPlayerEntity.getBlockStateAtPos().isOf(block))) {
         if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
            List<Vec3d> list = VectorMath.resolveList(this.antiCheatPlayer.vec3dList);
            int i = 0;

            for (Vec3d vec3d : list) {
               if (vec3d.y == list.getFirst().y) {
                  i++;
               }
            }

            if (i >= 10) {
               this.handleText(String.format("Y: %s Ticks: %.2f", i, list.getFirst().y));
            }
         }
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getFly().getValue();
   }
}
