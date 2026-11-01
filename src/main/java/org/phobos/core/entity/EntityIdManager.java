package org.phobos.core.entity;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.Entity;
import org.earthhack.ducks.ITotemPoppingEntity;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.data.EntityRecord;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;

public class EntityIdManager extends Feature {
   public final TimeGate.Threaded threaded = new TimeGate.Threaded();
   public final Map<Integer, Integer> integerMap = new HashMap<>();
   public static final Logger LOGGER = LoggerFactory.getLogger(EntityIdManager.class);

   public boolean testLong(long l) {
      return this.threaded.testLong(l);
   }

   public EntityIdManager() {
      this.handleListener(new Listener<EntityStatusEvent>(1000) {
         public void handleObject2(EntityStatusEvent entityStatusEvent) {
            if (entityStatusEvent.entity().equals(MinecraftHolder.mc.player)) {
               EntityIdManager.this.threaded.run();
            }

            if (entityStatusEvent.entity() instanceof ITotemPoppingEntity iTotemPoppingEntity) {
               iTotemPoppingEntity.lastTotemPop().run();
            }

            EntityIdManager.this.integerMap.compute(entityStatusEvent.entity().getId(), (integer, integer2) -> integer2 == null ? 1 : integer2 + 1);
         }
      });
      this.handleListener(new Listener<EntityRecord>() {
         public void handleObject2(EntityRecord entityRecord) {
            EntityIdManager.this.integerMap.remove(entityRecord.entity().getId());
         }
      });
      ModuleListenerUtils.handleListenerHolder3(this, this.integerMap::clear);
   }

   public TimeGate.Threaded getThreaded() {
      return this.threaded;
   }

   public int computeEntity(Entity entity) {
      return this.integerMap.getOrDefault(entity.getId(), -1);
   }
}
