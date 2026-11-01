package org.phobos.module.player;

import net.minecraft.client.gui.screen.DeathScreen;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.DeathScreenEvent;
import org.phobos.event.InitEvent;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class AutoRespawnModule extends Module {
   public void handleCancellableEvent(CancellableEvent cancellableEvent) {
      if (mc.player != null) {
         mc.player.requestRespawn();
      }

      cancellableEvent.setCancelled(true);
   }

   public AutoRespawnModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoRespawn", Categories.player, "Automatically respawns you");
      this.handleListener(new Listener<InitEvent<DeathScreen>>() {
         public void handleObject2(InitEvent<DeathScreen> initEvent) {
            if (initEvent.getScreen() instanceof DeathScreen) {
               AutoRespawnModule.this.handleCancellableEvent(initEvent);
            }
         }
      });
      this.handleListener(new Listener<DeathScreenEvent>() {
         public void handleObject2(DeathScreenEvent deathScreenEvent) {
            AutoRespawnModule.this.handleCancellableEvent(deathScreenEvent);
         }
      });
   }
}
