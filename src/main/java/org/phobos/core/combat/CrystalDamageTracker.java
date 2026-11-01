package org.phobos.core.combat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SetEvent;

public class CrystalDamageTracker {
   public final Map<Entity, CrystalDamageTracker.DamageUpdate> entityMap;
   public final Map<EndCrystalEntity, CrystalDamageTracker.DamagePrediction> endCrystalEntityMap = new ConcurrentHashMap<>();

   public void run() {
      this.entityMap.entrySet().removeIf(entry -> entry.getValue().check());
   }

   public void handleSetEvent(SetEvent setEvent) {
      CrystalDamageTracker.DamageUpdate damageUpdate = this.entityMap.get(setEvent.getLivingEntity());
      if (damageUpdate != null) {
         if (!(setEvent.getFloatValue() >= 0.0F)) {
            damageUpdate.result().accept(damageUpdate.damage(), setEvent.getFloatValue());
         }
      }
   }

   public void handleEntity(Entity entity, EndCrystalEntity endCrystalEntity, float f, BiConsumer<Float, Float> biConsumer) {
      if (entity != null) {
         this.endCrystalEntityMap.put(endCrystalEntity, new CrystalDamageTracker.DamagePrediction(entity, f, biConsumer));
      }
   }

   public CrystalDamageTracker() {
      this.entityMap = new ConcurrentHashMap<>();
   }

   public void handleRemoveEntityEvent(RemoveEntityEvent removeEntityEvent) {
      if (removeEntityEvent.getEntity() instanceof EndCrystalEntity endCrystalEntity) {
         CrystalDamageTracker.DamagePrediction damagePrediction = this.endCrystalEntityMap.get(endCrystalEntity);
         if (damagePrediction != null) {
            this.entityMap
               .put(
                  damagePrediction.entity(),
                  new CrystalDamageTracker.DamageUpdate(damagePrediction.breakDamage(), System.currentTimeMillis(), damagePrediction.result())
               );
         }
      }
   }

   private record DamagePrediction(Entity entity, float breakDamage, BiConsumer<Float, Float> result) {
   }

   private record DamageUpdate(float damage, long timestamp, BiConsumer<Float, Float> result) {

      public boolean check() {
         return System.currentTimeMillis() + 3000L < this.timestamp;
      }
   }
}
