package org.phobos.core.anticheat.check;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.block.BlockUtils;

public class GroundSpoofBCheck extends AntiCheatCheck {
   public static boolean testClientWorld(ClientWorld clientWorld, BlockPos blockPos) {
      if (!BlockUtils.testClientWorld2(clientWorld, clientWorld.getBlockState(blockPos).getBlock())) {
         return false;
      }

      int i = 0;
      List<BlockPos> list = new ArrayList<>();
      list.add(blockPos.east());
      list.add(blockPos.east().north());
      list.add(blockPos.west());
      list.add(blockPos.west().south());
      list.add(blockPos.north());
      list.add(blockPos.north().west());
      list.add(blockPos.south());
      list.add(blockPos.south().east());

      for (BlockPos blockPos2 : list) {
         if (BlockUtils.testClientWorld2(clientWorld, clientWorld.getBlockState(blockPos2).getBlock())) {
            i++;
         }
      }

      return i >= 8;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getGround().getValue();
   }

   @Override
   public void run3() {
      if (mc.world != null) {
         if (!this.antiCheatPlayer.flag10
            && Math.floor(this.antiCheatPlayer.vec3d3.y) == this.antiCheatPlayer.vec3d3.y
            && testClientWorld(mc.world, BlockPos.ofFloored(this.antiCheatPlayer.vec3d3).down())) {
            this.handleText("Ground: false");
            this.run();
         }
      }
   }

   public GroundSpoofBCheck(AntiCheatPlayer antiCheatPlayer) {
      super("GroundSpoofB", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 15;
   }

   public void run() {
      this.antiCheatPlayer.flag10 = true;
   }
}
