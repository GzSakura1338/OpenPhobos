package org.phobos.core.task;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.api.TimeGate;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.core.Feature;

public class ThreadedCooldown extends Feature {
   public final TimeGate.Threaded threaded = new TimeGate.Threaded();

   public ThreadedCooldown() {
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>() {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ThreadedCooldown.this.threaded.run();
            }
         }
      );
   }

   public boolean testLong(long l) {
      return this.threaded.testLong(l);
   }
}
