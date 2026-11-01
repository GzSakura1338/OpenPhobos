package org.phobos.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import org.phobos.core.event.events.CancellableEvent;

public class RemoveEntityEvent extends CancellableEvent {
   public final Entity entity;
   public final RemovalReason removalReason;

   public RemoveEntityEvent(Entity entity, RemovalReason removalReason) {
      this.entity = entity;
      this.removalReason = removalReason;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public RemovalReason getRemovalReason() {
      return this.removalReason;
   }
}
