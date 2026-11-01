package org.phobos.event;

import org.phobos.core.event.events.CancellableEvent;

public class CancelListener<E extends CancellableEvent> extends Listener<E> {
   public void handleObject2(E cancellableEvent) {
      cancellableEvent.setCancelled(true);
   }

   public CancelListener(Class<E> classValue) {
      super(classValue);
   }
}
