package org.phobos.module.misc;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.JumpEvent2;
import org.phobos.event.Listener;
import org.phobos.event.OnDeathScreenEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoDisableModule extends Module {
   public final Setting<Map<AutoDisableModule.EventType, Boolean>> trigger = new MultiSelectSetting<>("Trigger", AutoDisableModule.EventType.class)
      .resolveMultiSelectSetting5(multiSelectSetting -> {
         for (AutoDisableModule.EventType eventType : AutoDisableModule.EventType.values()) {
            multiSelectSetting.resolveMultiSelectSetting2(eventType, eventType.name(), false);
         }
      })
      .resolveSetting6(this);
   public final Setting<Map<Module, Boolean>> modules = new MultiSelectSetting<>("Modules", Module.class).resolveMultiSelectSetting5(multiSelectSetting -> {
      for (Module module : this.phobosClient.getModuleManager().getList()) {
         multiSelectSetting.resolveMultiSelectSetting2(module, module.getName(), false);
      }
   }).resolveSetting6(this);

   public AutoDisableModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoDisable", Categories.misc, "Automatically disables selected modules on certain events");
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoDisableModule.this.trigger.getValue().containsKey(AutoDisableModule.EventType.Lag)) {
                  if (!phobosClient.getTickManager().testLong(50L)) {
                     for (Entry<Module, Boolean> entry : AutoDisableModule.this.modules.getValue().entrySet()) {
                        if (entry.getValue()) {
                           entry.getKey().run11();
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<DisconnectEvent2>() {
         public void handleObject2(DisconnectEvent2 disconnectEvent2) {
            if (AutoDisableModule.this.trigger.getValue().containsKey(AutoDisableModule.EventType.Disconnect)) {
               for (Entry<Module, Boolean> entry : AutoDisableModule.this.modules.getValue().entrySet()) {
                  if (entry.getValue()) {
                     entry.getKey().run11();
                  }
               }
            }
         }
      });
      this.handleListener(new Listener<OnDeathScreenEvent>() {
         public void handleObject2(OnDeathScreenEvent onDeathScreenEvent) {
            if (AutoDisableModule.this.trigger.getValue().containsKey(AutoDisableModule.EventType.Death)) {
               for (Entry<Module, Boolean> entry : AutoDisableModule.this.modules.getValue().entrySet()) {
                  if (entry.getValue()) {
                     entry.getKey().run11();
                  }
               }
            }
         }
      });
      this.handleListener(new Listener<JumpEvent2>() {
         public void handleObject2(JumpEvent2 jumpEvent2) {
            if (AutoDisableModule.this.trigger.getValue().containsKey(AutoDisableModule.EventType.Jump)) {
               for (Entry<Module, Boolean> entry : AutoDisableModule.this.modules.getValue().entrySet()) {
                  if (entry.getValue()) {
                     entry.getKey().run11();
                  }
               }
            }
         }
      });
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   private enum EventType {
      Lag,
      Disconnect,
      Jump,
      Death;
   }
}
