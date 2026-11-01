package org.phobos.core.anticheat.check;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;

public class GroundSpoofACheck extends AntiCheatCheck {
   public GroundSpoofACheck(AntiCheatPlayer antiCheatPlayer) {
      super("GroundSpoofA", antiCheatPlayer);
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getGround().getValue();
   }

   public void run() {
      this.antiCheatPlayer.flag10 = false;
   }

   @Override
   public int value() {
      return 15;
   }

   @Override
   public void run3() {
      if (this.antiCheatPlayer.flag5 && this.antiCheatPlayer.flag6 && this.antiCheatPlayer.flag10) {
         BlockPos blockPos = BlockPos.ofFloored(this.antiCheatPlayer.vec3d3).down();
         assert mc.world != null;
         if (this.testClientWorld(mc.world, blockPos)) {
            this.handleText("spoof onGround=true");
            this.run();
         }
      }
   }

   public boolean testClientWorld(ClientWorld clientWorld, BlockPos blockPos) {
      if (clientWorld.getBlockState(blockPos).isAir() && clientWorld.getBlockState(blockPos.down()).isAir()) {
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
            if (clientWorld.getBlockState(blockPos2).isAir()) {
               i++;
            }
         }

         return i >= 8;
      } else {
         return false;
      }
   }
}
