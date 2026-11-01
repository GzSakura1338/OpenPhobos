package org.phobos.core.world;

import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.core.Feature;

public class WorldHolder extends Feature {
   @Nullable
   public volatile ClientWorld clientWorld;

   public WorldHolder() {
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            ClientWorld clientWorld = MinecraftHolder.mc.world;
            if (clientWorld == null) {
               WorldHolder.this.clientWorld = null;
            } else {
               WorldHolder.this.clientWorld = new EntityLookupWorld(clientWorld);
            }
         }
      });
   }

   @NotNull
   public ClientWorld getClientWorld() {
      ClientWorld clientWorld = this.clientWorld;
      if (clientWorld == null) {
         clientWorld = mc.world;
         if (clientWorld != null && mc.isOnThread()) {
            this.clientWorld = new EntityLookupWorld(clientWorld);
            clientWorld = this.clientWorld;
         }
      }

      return clientWorld;
   }
}
