package org.phobos.core.net;

import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.core.fakeplayer.MovingFakePlayer;

public final class LagState {
   public static boolean flag;
   public static MovingFakePlayer movingFakePlayer;

   public static double value() {
      return movingFakePlayer.getZ();
   }

   public static void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (movingFakePlayer == null || movingFakePlayer.clientWorld != clientPlayerEntity.clientWorld) {
         movingFakePlayer = new MovingFakePlayer(clientPlayerEntity.clientWorld);
      }

      movingFakePlayer.setPosition(clientPlayerEntity.getPos());
      movingFakePlayer.lastRenderX = clientPlayerEntity.getX();
      movingFakePlayer.lastRenderY = clientPlayerEntity.getY();
      movingFakePlayer.lastRenderZ = clientPlayerEntity.getZ();
   }

   public static double value2() {
      return movingFakePlayer.getY();
   }

   public static double value3() {
      return movingFakePlayer.getX();
   }

   public LagState() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
