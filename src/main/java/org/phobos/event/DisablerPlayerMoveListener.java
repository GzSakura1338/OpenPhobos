package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;

public final class DisablerPlayerMoveListener extends ModuleListener.Safe<DisablerModule, PlayerMoveEvent> {
   public DisablerPlayerMoveListener(DisablerModule disablerModule) {
      super(disablerModule, PlayerMoveEvent.class);
   }

   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((DisablerModule.Mode)this.module.mode.getValue()) {
         case Watchdog:
            if (!this.module.motion.getValue()) {
               return;
            }

            this.module.intValue4 = clientPlayerEntity.isOnGround() ? 0 : this.module.intValue4 + 1;
            if (this.module.flag11 && this.module.intValue4 >= 10) {
               if (this.module.intValue4 % 2 == 0) {
                  playerMoveEvent.setDoubleValue3(playerMoveEvent.getDoubleValue2() + 0.095);
               }

               clientPlayerEntity.setVelocity(0.0, 0.0, 0.0);
            }
            break;
         case Vanilla:
            if (this.module.flag2) {
               return;
            }

            double d = MathHelper.magnitude(
               clientPlayerEntity.getX() - clientPlayerEntity.lastX,
               clientPlayerEntity.getY() - clientPlayerEntity.lastBaseY,
               clientPlayerEntity.getZ() - clientPlayerEntity.lastZ
            );
            int i2 = (int)(d / this.module.packets.getValue().floatValue());
            if (i2 == 0) {
               return;
            }

            for (int i = 0; i < i2; i++) {
               PlayerMoveC2SPacket playerMoveC2SPacket = this.module.vanillaPacket.getValue().create(clientPlayerEntity, true);
               IPlayerMoveC2SPacket iPlayerMoveC2SPacket = (IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket;
               iPlayerMoveC2SPacket.setX(clientPlayerEntity.lastX);
               iPlayerMoveC2SPacket.setY(clientPlayerEntity.lastBaseY);
               iPlayerMoveC2SPacket.setZ(clientPlayerEntity.lastZ);
               clientPlayerEntity.networkHandler.sendPacket(playerMoveC2SPacket);
            }
      }
   }
}
