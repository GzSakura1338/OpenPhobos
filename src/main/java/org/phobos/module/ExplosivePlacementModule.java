package org.phobos.module;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.event.SafeListener;
import org.phobos.core.module.Category;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.RenderContext;
import org.phobos.core.setting.Setting;
import org.phobos.core.event.events.UnusedEvent;

public abstract class ExplosivePlacementModule extends PlacementModule {
   public final Setting<ExplosivePlacementModule.Update> update = this.resolveSetting2(new EnumSetting<>("Update", ExplosivePlacementModule.Update.Sync));

   public ExplosivePlacementModule(PhobosClient phobosClient, String text, String text2, Category category, int i) {
      super(phobosClient, text, text2, category, i);
      this.handleListener(
         new SafeListener<UnusedEvent>() {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ExplosivePlacementModule.this.update.getValue().equals(ExplosivePlacementModule.Update.Sync)) {
                  ExplosivePlacementModule.this.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ExplosivePlacementModule.this.update.getValue().equals(ExplosivePlacementModule.Update.Fast)) {
                  ExplosivePlacementModule.this.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               }
            }
         }
      );
   }

   public ExplosivePlacementModule(PhobosClient phobosClient, String text, Category category, int i) {
      this(phobosClient, text, "", category, i);
   }

   public enum Update {
      Sync,
      Fast;
   }
}
