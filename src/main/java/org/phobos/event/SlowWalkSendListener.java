package org.phobos.event;

import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.SlowWalkModule;

public final class SlowWalkSendListener extends ModuleListener<SlowWalkModule, PacketEvent.Send<?>> {
   public SlowWalkSendListener(SlowWalkModule slowWalkModule) {
      super(slowWalkModule, PacketEvent.Send.class);
   }

   public void handleObject2(PacketEvent.Send<?> send) {
   }
}
