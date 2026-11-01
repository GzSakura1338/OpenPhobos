package org.phobos.event;

import org.phobos.module.movement.PacketFlyModule;

public final class PacketFlyTickListener extends ModuleListener<PacketFlyModule, TickEvent2> {
   public void handleObject2(TickEvent2 tickEvent2) {
      this.module.integerMap.entrySet().removeIf(entry -> entry.getValue().time() + 30000L < System.currentTimeMillis());
   }

   public PacketFlyTickListener(PacketFlyModule packetFlyModule) {
      super(packetFlyModule, TickEvent2.class);
   }
}
