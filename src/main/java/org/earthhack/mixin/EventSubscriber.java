package org.earthhack.mixin;

import org.jetbrains.annotations.Nullable;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.ClientHolder;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface EventSubscriber {
   default <E> E invoke(E event) {
      ClientHolder.getEventBus().handleObject2(event);
      return event;
   }

   default <E extends CancellableEvent> void invoke(E event, @Nullable Class<?> generic, CallbackInfo ci) {
      ClientHolder.getEventBus().handleObject3(event, generic);
      if (event.isCancelled()) {
         ci.cancel();
      }
   }

   default <E extends CancellableEvent> E invoke(E event, CallbackInfo ci) {
      ClientHolder.getEventBus().handleObject2(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

      return event;
   }

   default <E> E invoke(E event, @Nullable Class<?> generic) {
      ClientHolder.getEventBus().handleObject3(event, generic);
      return event;
   }
}
