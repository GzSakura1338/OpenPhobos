package org.phobos.module;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.api.EntityTargetFilter;
import org.phobos.api.EntityTargetFilter;
import org.phobos.module.Module;
import org.phobos.core.module.Category;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class TargetModule extends Module implements EntityTargetFilter {
   public final Setting<Map<EntityTargetFilter.Target, Boolean>> targets = new MultiSelectSetting<>("Targets", EntityTargetFilter.Target.class)
      .resolveMultiSelectSetting5(multiSelectSetting -> {
         for (EntityTargetFilter.Target target : EntityTargetFilter.Target.values()) {
            multiSelectSetting.resolveMultiSelectSetting(target, target.name());
         }
      })
      .resolveSetting6(this);

   @OverrideOnly
   public boolean testLivingEntity(LivingEntity livingEntity) {
      return true;
   }

   public TargetModule(PhobosClient phobosClient, String text, Category category, String text2) {
      this(phobosClient, text, text2, category);
   }

   public Setting<Map<EntityTargetFilter.Target, Boolean>> getTargets() {
      return this.targets;
   }

   public TargetModule(PhobosClient phobosClient, String text, Category category) {
      this(phobosClient, text, "", category);
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, double d, Consumer<LivingEntity> consumer, Runnable... runnables
   ) {
      MutableDouble mutableDouble = new MutableDouble();
      MutableObject<LivingEntity> mutableObject = new MutableObject();

      for (Entity entity : clientWorld.getEntities()) {
         if (entity instanceof LivingEntity livingEntity
            && !livingEntity.isDead()
            && !Objects.equals(clientPlayerEntity, livingEntity)
            && this.testEntity2(livingEntity)
            && this.testLivingEntity(livingEntity)
            && !(livingEntity instanceof PlayerEntity playerEntity && this.phobosClient.getFriendList().testUUID(playerEntity.getUuid()))) {
            double d2 = clientPlayerEntity.squaredDistanceTo(livingEntity);
            if (!(d2 > d * d)) {
               if (mutableObject.getValue() == null) {
                  mutableObject.setValue(livingEntity);
                  mutableDouble.setValue(d2);
               } else if (d2 < mutableDouble.getValue()) {
                  mutableObject.setValue(livingEntity);
                  mutableDouble.setValue(d2);
               }
            }
         }
      }

      LivingEntity livingEntity2 = (LivingEntity)mutableObject.getValue();
      if (livingEntity2 == null) {
         Arrays.stream(runnables).forEach(Runnable::run);
      } else {
         consumer.accept(livingEntity2);
      }
   }

   public TargetModule(PhobosClient phobosClient, String text, String text2, Category category) {
      super(phobosClient, text, text2, category);
   }

   @Override
   public Setting<Map<EntityTargetFilter.Target, Boolean>> getTargets2() {
      return this.targets;
   }
}
