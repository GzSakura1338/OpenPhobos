package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.player.SneakModule;

public final class SneakPlayerTickListener extends ModuleListener.Safe<SneakModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (PlayerUtils.check()) {
         this.module.handleClientPlayerEntity4(clientPlayerEntity);
         this.module.handleClientPlayerEntity(clientPlayerEntity);
      }
   }

   public SneakPlayerTickListener(SneakModule sneakModule) {
      super(sneakModule, PlayerTickEvent.class);
   }
}
