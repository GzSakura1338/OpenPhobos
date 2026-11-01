package org.phobos.core.entity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import org.phobos.event.Listener;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.ShouldRenderEvent;
import org.phobos.core.Feature;

public class DeadEntityTracker extends Feature {
   public final Set<Entity> entitySet = ConcurrentHashMap.newKeySet();

   public void handleEntity2(Entity entity) {
      this.entitySet.remove(entity);
   }

   public void handleEntity(Entity entity) {
      if (mc.world != null) {
         mc.submit(() -> {
            if (mc.world != null) {
               mc.world.removeEntity(entity.getId(), RemovalReason.DISCARDED);
            }
         });
      }
   }

   public DeadEntityTracker() {
      this.handleListener(new Listener<ShouldRenderEvent<?>>(Integer.MAX_VALUE) {
         public void handleObject2(ShouldRenderEvent<?> shouldRenderEvent) {
            if (DeadEntityTracker.this.entitySet.contains(shouldRenderEvent.getEntity())) {
               shouldRenderEvent.setCancelled(true);
            }
         }
      });
      this.handleListener(new Listener<RemoveEntityEvent>(Integer.MAX_VALUE) {
         public void handleObject2(RemoveEntityEvent removeEntityEvent) {
            DeadEntityTracker.this.entitySet.removeIf(entity -> entity.equals(removeEntityEvent.getEntity()));
         }
      });
   }

   public void handleSetDeadType(DeadEntityTracker.SetDeadType setDeadType, Entity entity) {
      switch (setDeadType) {
         case Kill:
            this.handleEntity(entity);
            break;
         case Visibility:
            this.entitySet.add(entity);
            break;
         case Full:
            this.handleEntity(entity);
            this.entitySet.add(entity);
      }
   }

   public enum SetDeadType {
      Kill,
      Visibility,
      Full;
   }
}
