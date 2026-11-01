package org.phobos.core.event;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.ClientHolder;

public final class EventCallbackUtils {
   public static void handleCancellableEvent(CancellableEvent cancellableEvent, @Nullable Class<?> classValue, CallbackInfo callbackInfo) {
      ClientHolder.getEventBus().handleObject3(cancellableEvent, classValue);
      if (cancellableEvent.isCancelled()) {
         callbackInfo.cancel();
      }
   }

   public static void handleObject(Object object) {
      ClientHolder.getEventBus().handleObject2(object);
   }

   public EventCallbackUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleCancellableEvent2(CancellableEvent cancellableEvent, CallbackInfo callbackInfo) {
      ClientHolder.getEventBus().handleObject2(cancellableEvent);
      if (cancellableEvent.isCancelled()) {
         callbackInfo.cancel();
      }
   }
}
