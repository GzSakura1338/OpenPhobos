package org.phobos.event;

import net.minecraft.entity.Entity;
import org.phobos.core.event.events.CancellableEvent;

public class IsGlowingEvent extends CancellableEvent {
   public boolean flag;
   public final Entity entity;

   public boolean isFlag() {
      return this.flag;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public IsGlowingEvent(Entity entity, boolean flag) {
      this.entity = entity;
      this.flag = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }
}
