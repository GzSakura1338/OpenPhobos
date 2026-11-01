package org.phobos.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.WorldRenderer;

public interface WorldRendererCallback {
   Event<WorldRendererCallback> worldRendererCallbackEvent = EventFactory.createArrayBacked(WorldRendererCallback.class, worldRendererCallbacks -> worldRenderer -> {
      for (WorldRendererCallback worldRendererCallback : worldRendererCallbacks) {
         worldRendererCallback.handleWorldRenderer(worldRenderer);
      }
   });

   void handleWorldRenderer(WorldRenderer var1);
}
