package org.phobos.module.misc;

import java.util.List;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.core.kit.KitRegistry;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.kit.RegearSlot;
import org.phobos.core.kit.SlotMoveResult;
import org.phobos.core.kit.SlotPair;
import org.phobos.core.kit.SlotHandle;
import org.phobos.core.kit.RegearPlan;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.kit.Kit;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoRegearModule extends Module {
   public final TimeGate.Normal normal;
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 50L, 0L, 500L, true));

   public void handleItemSwitcher(ItemSwitcher itemSwitcher, @Nullable SlotHandle slotHandle, ScreenHandler screenHandler) {
      if (slotHandle != null) {
         itemSwitcher.handleInt4(slotHandle.getSlot().id, 40, SlotActionType.SWAP, screenHandler);
      }
   }

   public AutoRegearModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoRegear", Categories.misc, "Regears you");
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<TickEvent2>(400) {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (KitRegistry.getINSTANCE().getKitMutableObject().getValue() != null) {
                  if (AutoRegearModule.this.normal.testNumber2(AutoRegearModule.this.delay.getValue())) {
                     if (mc.currentScreen instanceof ShulkerBoxScreen shulkerBoxScreen) {
                        ShulkerBoxScreenHandler shulkerBoxScreenHandler = (ShulkerBoxScreenHandler)shulkerBoxScreen.getScreenHandler();
                        List<Slot> list = InventoryUtils.resolveList3(shulkerBoxScreenHandler);
                        if (AutoRegearModule.this.testClientPlayerEntity(clientPlayerEntity, list, shulkerBoxScreenHandler)) {
                           KitRegistry.getINSTANCE().handleInt(list.size());
                        } else {
                           KitRegistry.getINSTANCE().run();
                        }
                     } else if (mc.currentScreen instanceof GenericContainerScreen genericContainerScreen) {
                        GenericContainerScreenHandler genericContainerScreenHandler = (GenericContainerScreenHandler)genericContainerScreen.getScreenHandler();
                        List<Slot> list2 = InventoryUtils.resolveList2(genericContainerScreenHandler);
                        if (AutoRegearModule.this.testClientPlayerEntity(clientPlayerEntity, list2, genericContainerScreenHandler)) {
                           KitRegistry.getINSTANCE().handleInt(list2.size());
                        } else {
                           KitRegistry.getINSTANCE().run();
                        }
                     } else {
                        KitRegistry.getINSTANCE().run();
                     }
                  }
               }
            }
         }
      );
   }

   @Override
   public void run4() {
      KitRegistry.getINSTANCE().run();
   }

   public void handleItemSwitcher2(ItemSwitcher itemSwitcher, @Nullable SlotPair slotPair, ScreenHandler screenHandler) {
      if (slotPair != null) {
         Slot slot = slotPair.getSlot();
         Slot slot2 = slotPair.getSlot2();
         itemSwitcher.handleInt4(slot.id, 0, SlotActionType.PICKUP, screenHandler);
         itemSwitcher.handleInt4(slot2.id, 0, SlotActionType.PICKUP, screenHandler);
         itemSwitcher.handleInt4(slot.id, 0, SlotActionType.PICKUP, screenHandler);
      }
   }

   public void handleItemSwitcher3(ItemSwitcher itemSwitcher, @Nullable SlotMoveResult slotMoveResult, ScreenHandler screenHandler) {
      if (slotMoveResult != null) {
         Slot slot = slotMoveResult.getSlot();
         Slot slot2 = slotMoveResult.getSlot2();
         if (slotMoveResult.isEmpty()) {
            itemSwitcher.handleInt4(slot.id, 0, SlotActionType.PICKUP, screenHandler);
            itemSwitcher.handleInt4(slot2.id, 0, SlotActionType.PICKUP, screenHandler);
         } else {
            itemSwitcher.handleInt4(slot.id, 0, SlotActionType.PICKUP, screenHandler);
            itemSwitcher.handleInt4(slot2.id, 0, SlotActionType.PICKUP, screenHandler);
            itemSwitcher.handleInt4(slot.id, 0, SlotActionType.PICKUP, screenHandler);
         }
      }
   }

   public boolean testList(List<Slot> list) {
      for (Slot slot : list) {
         if (slot.getStack().isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void handleItemSwitcher4(ItemSwitcher itemSwitcher, @Nullable RegearSlot regearSlot, List<Slot> list, ScreenHandler screenHandler) {
      if (regearSlot != null) {
         if (this.testList(list)) {
            itemSwitcher.handleInt4(regearSlot.getSlot().id, 1, SlotActionType.THROW, screenHandler);
         } else {
            itemSwitcher.handleInt4(regearSlot.getSlot().id, 0, SlotActionType.QUICK_MOVE, screenHandler);
         }
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, List<Slot> list, ScreenHandler screenHandler) {
      MutableBoolean mutableBoolean = new MutableBoolean();
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> {
               RegearPlan regearPlan = ((Kit)KitRegistry.getINSTANCE().getKitMutableObject().getValue())
                  .resolveRegearPlan(clientPlayerEntity, list);
               regearPlan.handleRegearSingleton((slotMoveResult, regearSlot, slotPair, slotHandle) -> {
                  this.handleItemSwitcher4(itemSwitcher, regearSlot, list, screenHandler);
                  this.handleItemSwitcher(itemSwitcher, slotHandle, screenHandler);
                  this.handleItemSwitcher2(itemSwitcher, slotPair, screenHandler);
                  this.handleItemSwitcher3(itemSwitcher, slotMoveResult, screenHandler);
                  mutableBoolean.setTrue();
               });
            }
         );
      return mutableBoolean.getValue();
   }
}
