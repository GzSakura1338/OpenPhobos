package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.render.RenderContext;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;

public final class BreakerRenderContextListener extends ModuleListener.Safe<BreakerModule, RenderContext> {
   public void handleObject(
      RenderContext renderContext, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(BreakerModule.Mode.Phase) && this.module.update.testObject2(BreakerModule.Update.Sync)) {
         this.module.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, BreakerModule.Update.Sync);
      }
   }

   public BreakerRenderContextListener(BreakerModule breakerModule) {
      super(breakerModule, RenderContext.class);
   }
}
