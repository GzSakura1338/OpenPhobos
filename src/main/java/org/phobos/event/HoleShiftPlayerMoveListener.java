package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.tick.TickManager;
import org.phobos.core.tick.TickManager;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.module.movement.HoleShiftModule;

public final class HoleShiftPlayerMoveListener extends ModuleListener.Safe<HoleShiftModule, PlayerMoveEvent> {
   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.phobosClient.getTickManager().check()) {
         double d = clientPlayerEntity.getY() - clientPlayerEntity.prevY;
         if (!(d <= 0.5) && !(d > 2.0)) {
            Vec3d vec3d = new Vec3d(clientPlayerEntity.prevX, clientPlayerEntity.prevY, clientPlayerEntity.prevZ);
            MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
            movingFakePlayer.copyFrom(clientPlayerEntity);
            movingFakePlayer.setPosition(vec3d);
            if (this.phobosClient.getSurroundManager().testPlayerEntity(movingFakePlayer, true)) {
               this.phobosClient
                  .getTickManager()
                  .handleClientPlayerEntity3(
                     clientPlayerEntity, TickManager.TickData.createTickData(this.module.ticks.getValue()), TickManager.Tick.Player
                  );
               this.module.run11();
            }
         }
      }
   }

   public HoleShiftPlayerMoveListener(HoleShiftModule holeShiftModule) {
      super(holeShiftModule, PlayerMoveEvent.class);
   }
}
