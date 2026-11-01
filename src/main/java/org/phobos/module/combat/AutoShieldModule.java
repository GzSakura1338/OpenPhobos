package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;

public class AutoShieldModule extends Module {
   public final Setting<AutoShieldModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoShieldModule.Mode.Macro));
   public final Setting<Integer> ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 20, 2, 60));

   public AutoShieldModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoShield", Categories.combat, "Tries to block crystals with a shield");
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((AutoShieldModule.Mode)AutoShieldModule.this.mode.getValue()) {
                  case Macro:
                     phobosClient.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> itemSwitcher.handleRunnable3(
                              () -> {
                                 Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.SHIELD) ? slotx : null);
                                 if (slot != null) {
                                    SilentHotbarModule.getSilentHotbarManager()
                                       .handleObject3(
                                          AutoShieldModule.this,
                                          slot,
                                          4,
                                          SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                       );
                                    SwitchOverride.handleFlag3(true);
                                 }
                              }
                           )
                        );
                     break;
                  case Auto:
                     if (clientPlayerEntity.hurtTime != 10) {
                        return;
                     }

                     phobosClient.getScheduler()
                        .handleRunnable2(
                           () -> {
                              if (!clientPlayerEntity.isOnGround()) {
                                 phobosClient.getScheduler()
                                    .handleRunnable3(
                                       () -> phobosClient.getInventoryManager()
                                          .handleConsumer2(
                                             itemSwitcher -> itemSwitcher.handleRunnable3(
                                                () -> {
                                                   Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.SHIELD) ? slotx : null);
                                                   if (slot != null) {
                                                      SilentHotbarModule.getSilentHotbarManager()
                                                         .handleObject3(
                                                            AutoShieldModule.this,
                                                            slot,
                                                            2,
                                                            SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                         );
                                                      SwitchOverride.handleFlag3(true);
                                                   }
                                                }
                                             )
                                          ),
                                       SwitchOverride::run2,
                                       AutoShieldModule.this.ticks.getValue()
                                    );
                              }
                           },
                           2
                        );
               }
            }
         }
      );
   }

   @Override
   public void run4() {
      SwitchOverride.run2();
      SilentHotbarModule.getSilentHotbarManager().handleObject2(this);
   }

   private enum Mode {
      Macro,
      Auto;
   }
}
