package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;

public final class PacketFlyMoveListener extends ModuleListener.Safe<PacketFlyModule, MoveEvent2> {
   public PacketFlyMoveListener(PacketFlyModule packetFlyModule) {
      super(packetFlyModule, MoveEvent2.class);
   }

   public void handleObject(
      MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.getValue().equals(PacketFlyModule.Mode.Jitter) || this.module.atomicInteger.get() != 0) {
         moveEvent2.setDoubleValue3(clientPlayerEntity.getVelocity().getX());
         moveEvent2.setDoubleValue(clientPlayerEntity.getVelocity().getY());
         moveEvent2.setDoubleValue2(clientPlayerEntity.getVelocity().getZ());
         if (this.module.phase.getValue().equals(PacketFlyModule.Phase.Semi) || this.module.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
            clientPlayerEntity.noClip = true;
         }
      }
   }
}
