package org.phobos.event;

import net.minecraft.entity.Entity;
import org.phobos.core.event.events.CancellableEvent;

public class AttackEvent2 extends CancellableEvent {
   public final Entity entity;

   public AttackEvent2(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public static class Pre extends CancellableEvent {
      public final Entity entity;

      public Pre(Entity entity) {
         this.entity = entity;
      }

      public Entity getEntity() {
         return this.entity;
      }
   }
}
