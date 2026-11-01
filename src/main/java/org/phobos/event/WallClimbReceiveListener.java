package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.movement.WallClimbModule;

public final class WallClimbReceiveListener extends ModuleListener.Safe<WallClimbModule, PacketEvent.Receive<?>> {
   public void handleObject(
      PacketEvent.Receive<?> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(WallClimbModule.Mode.Polar)) {
         if (receive.getPacket() instanceof PlayerPositionLookS2CPacket) {
            if (!this.module.normal.testNumber(this.module.sequence.getValue()) && this.module.reset.getValue() && !this.module.check()) {
               this.module.run();
            }

            this.module.normal.run();
         }
      }
   }

   public WallClimbReceiveListener(WallClimbModule wallClimbModule) {
      super(wallClimbModule, PacketEvent.Receive.class);
   }
}
