package org.phobos.event;

import net.minecraft.entity.Entity;
import org.phobos.core.event.events.CancellableEvent;

public class AddEntityEvent extends CancellableEvent {
   public final Entity entity;

   public AddEntityEvent(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
