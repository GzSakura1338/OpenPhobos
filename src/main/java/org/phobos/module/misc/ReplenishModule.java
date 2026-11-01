package org.phobos.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.GameMode;
import org.earthhack.ducks.ISlot;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.math.FloatPair;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.inventory.ScheduledClick;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class ReplenishModule extends Module {
   public final Setting<Long> delay;
   public final Setting<Boolean> creative;
   public final Setting<NumberRange.Mutable<Float>> speed;
   public final Setting<Double> threshold;
   public final TimeGate.Normal normal;
   public final Setting<ReplenishModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ReplenishModule.Mode.Rage));

   public ReplenishModule(PhobosClient phobosClient) {
      super(phobosClient, "Replenish", Categories.misc, "Refills your hotbar");
      this.creative = this.resolveSetting2(new BooleanSetting("Creative").resolveSetting7("Replenish for singleplayer testing"));
      this.speed = this.resolveSetting2(
         new MutableNumberSetting<>("Speed", NumberRange.createMutable(50.0F, 150.0F), 10.0F, 200.0F)
            .resolveSetting7("Aim speed")
            .resolveSetting4(this.mode, ReplenishModule.Mode.Legit)
      );
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 50L, 0L, 500L, true));
      this.threshold = this.resolveSetting2(new NumberSetting<>("Threshold", 25.0, 1.0, 99.0, "%"));
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!ReplenishModule.this.normal.testNumber2(ReplenishModule.this.delay.getValue())) {
                  if (!ReplenishModule.this.mode.testObject2(ReplenishModule.Mode.Legit) || !ClickScheduler.getINSTANCE().isFlag()) {
                     phobosClient.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> {
                              for (int i4 = 36; i4 < 45; i4++) {
                                 Slot slot4 = clientPlayerEntity.playerScreenHandler.getSlot(i4);
                                 ItemStack itemStack2 = slot4.getStack();
                                 double d = InventoryUtils.computeItemStack2(itemStack2);
                                 if (slot4.hasStack() && d <= ReplenishModule.this.threshold.getValue()) {
                                    int i2 = itemStack2.getMaxCount() - itemStack2.getCount();
                                    if (ReplenishModule.this.creative.getValue()
                                       && mc.isInSingleplayer()
                                       && clientPlayerInteractionManager.getCurrentGameMode() != GameMode.CREATIVE
                                       && i2 > 0) {
                                       clientPlayerEntity.networkHandler
                                          .sendChatCommand(
                                             "give %s %s %s"
                                                .formatted(clientPlayerEntity.getGameProfile().getName(), Registries.ITEM.getId(itemStack2.getItem()), i2)
                                          );
                                    } else {
                                       int i3 = 0;
                                       Slot slot3 = null;

                                       for (int i = clientPlayerEntity.playerScreenHandler.slots.size() - 1; i >= 0; i--) {
                                          if (i < 36) {
                                             Slot slot = (Slot)clientPlayerEntity.playerScreenHandler.slots.get(i);
                                             ItemStack itemStack = slot.getStack();
                                             if (itemStack.isOf(itemStack2.getItem())
                                                && itemStack.getCount() > i3
                                                && itemSwitcher.testItemStack(itemStack, itemStack2)) {
                                                i3 = itemStack.getCount();
                                                slot3 = slot;
                                                if (i3 == 64) {
                                                   break;
                                                }
                                             }
                                          }
                                       }

                                       if (slot3 != null && slot3 != slot4 && i3 > itemStack2.getCount()) {
                                          switch ((ReplenishModule.Mode)ReplenishModule.this.mode.getValue()) {
                                             case Rage:
                                                Slot slot2 = slot3;
                                                itemSwitcher.handleRunnable2(() -> itemSwitcher.handleInt5(slot2.id, 0, SlotActionType.QUICK_MOVE));
                                                break;
                                             case Legit:
                                                ISlot iSlot = (ISlot)(Object)slot3;
                                                if (iSlot.isLoaded()) {
                                                   ClickScheduler.getINSTANCE()
                                                      .handleISlot(
                                                         iSlot,
                                                         FloatPair.createFloatPair(
                                                            ReplenishModule.this.speed.getValue().getNumber2(),
                                                            ReplenishModule.this.speed.getValue().getNumber3()
                                                         )
                                                      );
                                                   ClickScheduler.getINSTANCE().handleSlot(slot3, new ScheduledClick(0, SlotActionType.QUICK_MOVE));
                                                }

                                                return;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        );
                  }
               }
            }
         }
      );
   }

   private enum Mode {
      Rage,
      Legit;
   }
}
