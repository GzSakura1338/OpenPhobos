package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.module.player.XCarryModule;
import org.phobos.module.player.XCarryModule;

public final class XCarryUnusedListener extends ModuleListener.Safe<XCarryModule, UnusedEvent> {
   public void handleObject(
      UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.getValue().equals(XCarryModule.Mode.Auto)) {
         if (this.module.normal.testNumber(this.module.delay.getValue()) && !this.module.items.getValue().isEmpty()) {
            this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
               int i4 = -1;

               for (int i3 = 1; i3 <= 5; i3++) {
                  Slot slot3 = clientPlayerEntity.playerScreenHandler.getSlot(i3);
                  if (!slot3.hasStack()) {
                     if (i4 < 0) {
                        i4 = 0;

                        for (int i = 9; i < 36; i++) {
                           Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(i);
                           if (!slot.hasStack()) {
                              i4++;
                           }
                        }
                     }

                     if (i4 < 6) {
                        Item item = this.module.resolveItem(i3);
                        if (item != null && (!slot3.getStack().isOf(item) || slot3.getStack().getCount() != slot3.getStack().getMaxCount())) {
                           for (int i2 = 9; i2 < 36; i2++) {
                              Slot slot2 = clientPlayerEntity.playerScreenHandler.getSlot(i2);
                              if (slot2.hasStack() && slot2.getStack().isOf(item)) {
                                 itemSwitcher.handleSlot3(slot2, slot3);
                                 this.module.normal.run();
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            });
         }
      }
   }

   public XCarryUnusedListener(XCarryModule xCarryModule) {
      super(xCarryModule, UnusedEvent.class);
   }
}
