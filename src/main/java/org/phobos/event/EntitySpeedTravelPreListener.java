package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.core.event.events.TravelEvent;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.movement.EntitySpeedModule;

public final class EntitySpeedTravelPreListener extends ModuleListener.Safe<EntitySpeedModule, TravelEvent.Pre> {
   public void handleObject(
      TravelEvent.Pre pre, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Entity entity = clientPlayerEntity.getControllingVehicle();
      if (entity != null) {
         double[] doubles = PlayerUtils.values2(clientPlayerEntity, this.module.speed.getValue().floatValue());
         if (PlayerUtils.check()) {
            entity.setVelocity(doubles[0], entity.getVelocity().getY(), doubles[1]);
            pre.setCancelled(true);
         } else {
            entity.setVelocity(0.0, entity.getVelocity().getY(), 0.0);
            pre.setCancelled(true);
         }
      }
   }

   public EntitySpeedTravelPreListener(EntitySpeedModule entitySpeedModule) {
      super(entitySpeedModule, TravelEvent.Pre.class);
   }
}
