package org.phobos.core.command;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;

public class ClipCommand extends Command {
   public final ClipCommand.PositionAxis positionAxis;

   @Override
   public void handleString5(String[] texts) {
      try {
         if (mc.player == null) {
            this.handleText2("Unable to use this command at this time");
            return;
         }

         double d4 = Double.parseDouble(texts[0]);
         switch (this.positionAxis) {
            case Vertical:
               int i = (int)Math.ceil(Math.abs(d4 / 10.0));
               PlayerActionUtils.handleClientPlayerEntity20(mc.player, mc.world, i);
               mc.player.setPosition(mc.player.getX(), mc.player.getY() + d4, mc.player.getZ());
               mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + d4, mc.player.getZ(), mc.player.isOnGround()));
               break;
            case Horizontal:
               double d = Math.toRadians(mc.player.getYaw() + 90.0F);
               double d2 = Math.cos(d) * d4;
               double d3 = Math.sin(d) * d4;
               mc.player.setPosition(mc.player.getX() + d2, mc.player.getY(), mc.player.getZ() + d3);
               mc.player
                  .networkHandler
                  .sendPacket(new PositionAndOnGround(mc.player.getX() + d2, mc.player.getY(), mc.player.getZ() + d3, mc.player.isOnGround()));
         }
      } catch (Throwable throwable) {
         this.handleText2("Failed to parse arguments");
      }
   }

   public ClipCommand(PhobosClient phobosClient, String text, ClipCommand.PositionAxis positionAxis) {
      super(phobosClient, text, "blocks");
      this.positionAxis = positionAxis;
   }

   public enum PositionAxis {
      Vertical,
      Horizontal;
   }
}
