package org.phobos.event;

import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.SlowWalkModule;

public final class SlowWalkReceiveListener extends ModuleListener<SlowWalkModule, PacketEvent.Receive<?>> {
   public SlowWalkReceiveListener(SlowWalkModule slowWalkModule) {
      super(slowWalkModule, PacketEvent.Receive.class);
   }

   public void handleObject2(PacketEvent.Receive<?> receive) {
   }
}
