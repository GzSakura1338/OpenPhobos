package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.movement.JesusModule;

public final class JesusTickListener extends ModuleListener.Safe<JesusModule, TickEvent2> {
   public JesusTickListener(JesusModule jesusModule) {
      super(jesusModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!(clientPlayerEntity.fallDistance >= 3.0F)) {
         if ((clientPlayerEntity.isInLava() || clientPlayerEntity.isTouchingWater()) && !clientPlayerEntity.isSneaking()) {
            PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.1);
         } else {
            if (clientPlayerEntity.isSneaking() && this.module.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
               PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -0.1);
            }
         }
      }
   }
}
