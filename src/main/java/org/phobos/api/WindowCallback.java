package org.phobos.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public interface WindowCallback {
   Event<WindowCallback> windowCallbackEvent = EventFactory.createArrayBacked(WindowCallback.class, windowCallbacks -> (minecraftClient, window) -> {
      for (WindowCallback windowCallback : windowCallbacks) {
         windowCallback.handleMinecraftClient(minecraftClient, window);
      }
   });

   void handleMinecraftClient(MinecraftClient var1, Window var2);
}
