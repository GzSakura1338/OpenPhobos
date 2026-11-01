package org.phobos.event;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.misc.AutoReconnectModule;

public final class AutoReconnectTickListener extends ModuleListener.Safe<AutoReconnectModule, TickEvent2> {
   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.run6();
   }

   public void handleObject2(TickEvent2 tickEvent2) {
      if (mc.currentScreen instanceof DisconnectedScreen disconnectedScreen) {
         this.module.handleDisconnectedScreen(disconnectedScreen);
      }

      super.handleObject2(tickEvent2);
   }

   public AutoReconnectTickListener(AutoReconnectModule autoReconnectModule) {
      super(autoReconnectModule, TickEvent2.class);
   }
}
