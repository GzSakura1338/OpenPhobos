package org.phobos.event;

import net.minecraft.entity.Entity;
import org.phobos.core.event.events.CancellableEvent;

public class ShouldRenderEvent<E extends Entity> extends CancellableEvent {
   public final E entity;

   public ShouldRenderEvent(E entity) {
      this.entity = entity;
   }

   public E getEntity() {
      return this.entity;
   }
}
