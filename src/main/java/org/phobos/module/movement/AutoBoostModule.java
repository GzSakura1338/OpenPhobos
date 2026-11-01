package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.module.Module;
import org.phobos.module.combat.AuraModule;

public class AutoBoostModule extends Module {
   public static float floatValue;
   public static boolean flag = false;

   @Override
   public void run4() {
      flag = false;
   }

   public AutoBoostModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoBoost", Categories.movement, "Runs towards Aura target");
      this.handleListener(
         new SafeListener<PlayerTickEvent>(8) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Entity entity = AuraModule.getEntity();
               if (entity == null) {
                  AutoBoostModule.flag = false;
               } else {
                  clientPlayerEntity.setSprinting(true);
                  float[] floats = RotationUtils.values(clientPlayerEntity, entity);
                  phobosClient.getRotationManager().testFloat2(floats[0], clientPlayerEntity.getPitch(), true);
                  AutoBoostModule.floatValue = floats[0];
                  AutoBoostModule.flag = true;
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent>(1) {
            public void handleObject(
               TickEvent tickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!tickEvent.isCancelled()) {
                  Entity entity = AuraModule.getEntity();
                  if (entity != null) {
                     clientPlayerEntity.input.movementForward = 1.0F;
                     clientPlayerEntity.input.movementSideways = 0.0F;
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
