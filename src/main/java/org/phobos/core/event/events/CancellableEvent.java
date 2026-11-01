package org.phobos.core.event.events;

public class CancellableEvent {
   public boolean cancelled;

   public void setCancelled(boolean flag) {
      this.cancelled = flag;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }
}
