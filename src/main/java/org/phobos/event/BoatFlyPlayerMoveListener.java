package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.vehicle.BoatEntity;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.movement.BoatFlyModule;

public final class BoatFlyPlayerMoveListener extends ModuleListener.Safe<BoatFlyModule, PlayerMoveEvent> {
   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (clientPlayerEntity.hasVehicle() && clientPlayerEntity.getVehicle() instanceof BoatEntity boatEntity) {
         clientPlayerEntity.setYaw(boatEntity.getYaw());
         clientPlayerEntity.setPitch(90.0F);
         this.module.flag = true;
      }

      if (this.module.flag) {
         if (clientPlayerEntity.hasVehicle()) {
            mc.options.sneakKey.setPressed(true);
            return;
         }

         PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 1.8);
         this.module.flag = false;
         mc.options.sneakKey.setPressed(false);
         this.module.run11();
      }
   }

   public BoatFlyPlayerMoveListener(BoatFlyModule boatFlyModule) {
      super(boatFlyModule, PlayerMoveEvent.class);
   }
}
