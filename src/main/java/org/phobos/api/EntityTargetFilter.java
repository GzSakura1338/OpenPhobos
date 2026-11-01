package org.phobos.api;

import java.util.Map;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;
import org.phobos.core.setting.Setting;

public interface EntityTargetFilter {
   default boolean testEntity2(Entity entity) {
      if (entity == null) {
         return false;
      } else {
         Map<EntityTargetFilter.Target, Boolean> map = this.getTargets2().getValue();
         if (entity.isInvisible() && !map.get(EntityTargetFilter.Target.Invisibles)) {
            return false;
         } else if (entity instanceof AbstractClientPlayerEntity) {
            return map.get(EntityTargetFilter.Target.Players);
         } else if (entity instanceof Monster) {
            return map.get(EntityTargetFilter.Target.Hostiles);
         } else if (!(entity instanceof PassiveEntity) && !(entity instanceof AmbientEntity) && !(entity instanceof SquidEntity)) {
            return entity instanceof Angerable angerable && !angerable.hasAngerTime() ? map.get(EntityTargetFilter.Target.Neutral) : false;
         } else {
            return map.get(EntityTargetFilter.Target.Animals);
         }
      }
   }

   Setting<Map<EntityTargetFilter.Target, Boolean>> getTargets2();

   enum Target implements TextProvider {
      Players("Includes all players"),
      Hostiles("Hostile mobs like zombies"),
      Animals("Animals mobs like chickens"),
      Neutral("Neutral mobs like pigmen"),
      Invisibles("Any invisible entity");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Target(final String description) {
         this.text = description;
      }
   }
}
