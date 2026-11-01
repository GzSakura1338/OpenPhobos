package org.phobos.event;

import org.phobos.module.misc.NoInterpModule;

public final class NoInterpTrackedPositionUpdateListener extends ModuleListener<NoInterpModule, TrackedPositionUpdateEvent> {
   public void handleObject2(TrackedPositionUpdateEvent trackedPositionUpdateEvent) {
      trackedPositionUpdateEvent.getLivingEntity()
         .setPosition(trackedPositionUpdateEvent.getDoubleValue3(), trackedPositionUpdateEvent.getDoubleValue2(), trackedPositionUpdateEvent.getDoubleValue());
      trackedPositionUpdateEvent.getLivingEntity().setPitch(trackedPositionUpdateEvent.getFloatValue2());
      trackedPositionUpdateEvent.getLivingEntity().setYaw(trackedPositionUpdateEvent.getFloatValue());
   }

   public NoInterpTrackedPositionUpdateListener(NoInterpModule noInterpModule) {
      super(noInterpModule, TrackedPositionUpdateEvent.class);
   }
}
