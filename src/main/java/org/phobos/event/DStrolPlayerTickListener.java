package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.phobos.core.math.DistanceUtils;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.combat.DStrolModule;
import org.phobos.module.combat.DStrolModule;

public final class DStrolPlayerTickListener extends ModuleListener.Safe<DStrolModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.combat.getValue()) {
         if (clientPlayerEntity.hurtTime != 0) {
            this.module.flag = true;
            this.module.run5();
         }

         if (this.module.intValue > this.module.combatTicks.getValue() && this.module.flag) {
            this.module.flag = false;
            this.module.run2();
         }
      }

      Entity entity = AuraModule.getEntity();
      if (!(entity instanceof LivingEntity livingEntity)) {
         this.module.run2();
      } else {
         double d = DistanceUtils.computeClientPlayerEntity(clientPlayerEntity, entity);
         switch ((DStrolModule.Mode)this.module.mode.getValue()) {
            case Evaluate:
               if (this.module.testDouble2(d)
                  && (this.module.computeClientWorld(clientWorld, entity) > 0.13281025622712236 && entity.fallDistance > 0.0F || livingEntity.hurtTime != 0)
                  && clientPlayerEntity.hurtTime == 0) {
                  this.module.run5();
                  return;
               }

               if (this.module.release.getValue() && this.module.testDouble(d)) {
                  this.module.run();
                  return;
               }

               this.module.run2();
               break;
            case Distance:
               if (d <= this.module.stretch.getValue() && entity.fallDistance > 0.0F) {
                  this.module.run5();
                  return;
               }

               if (this.module.release.getValue() && this.module.testDouble(d)) {
                  this.module.run();
                  return;
               }

               this.module.run2();
         }
      }
   }

   public DStrolPlayerTickListener(DStrolModule dStrolModule) {
      super(dStrolModule, PlayerTickEvent.class);
   }
}
