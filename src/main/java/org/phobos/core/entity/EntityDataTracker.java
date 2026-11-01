package org.phobos.core.entity;

import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.api.EntityMapper;
import org.phobos.api.ListenerHolder;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.Feature;

public class EntityDataTracker<K extends Entity, V> extends Feature {
   public EntityDataTracker(Class<? super K> classValueArg, Map<K, V> map, EntityMapper<K, V> entityMapper) {
      this.handleListener(
         new SafeListener<AddEntityEvent>() {
            public void handleObject(
               AddEntityEvent addEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Entity entity = addEntityEvent.getEntity();
               if (classValueArg.isInstance(entity)) {
                  K entity2 = (K)entity;
                  map.put(entity2, entityMapper.resolveObject(entity2));
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Entity entity = removeEntityEvent.getEntity();
               if (classValueArg.isInstance(entity)) {
                  K entity2 = (K)entity;
                  map.remove(entity2);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (Entity entity2 : clientWorld.getEntities()) {
                  if (classValueArg.isInstance(entity2)) {
                     K entity = (K)entity2;
                     if (!map.containsKey(entity)) {
                        map.put(entity, entityMapper.resolveObject(entity));
                     }
                  }
               }
            }
         }
      );
   }

   public static <K extends Entity, V> EntityDataTracker<K, V> resolveEntityDataTracker(
      Class<? super K> classValue, Map<K, V> map, EntityMapper<K, V> entityMapper, ListenerHolder listenerHolder
   ) {
      return new EntityDataTracker<>(classValue, map, entityMapper).resolveEntityDataTracker2(listenerHolder);
   }

   public EntityDataTracker<K, V> resolveEntityDataTracker2(ListenerHolder listenerHolder) {
      listenerHolder.handleListenerHolder(this);
      return this;
   }
}
