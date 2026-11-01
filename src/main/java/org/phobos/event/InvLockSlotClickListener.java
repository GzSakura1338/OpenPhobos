package org.phobos.event;

import org.phobos.core.event.events.SlotClickEvent;
import org.phobos.module.player.InvLockModule;

public final class InvLockSlotClickListener extends ModuleListener<InvLockModule, SlotClickEvent> {
   public InvLockSlotClickListener(InvLockModule invLockModule) {
      super(invLockModule, SlotClickEvent.class);
   }

   public void handleObject2(SlotClickEvent slotClickEvent) {
      if (this.module.armor.getValue() && slotClickEvent.getIntValue2() >= 5 && slotClickEvent.getIntValue2() < 9) {
         slotClickEvent.setCancelled(true);
      }

      if (this.module.offhand.getValue() && slotClickEvent.getIntValue2() == 45) {
         slotClickEvent.setCancelled(true);
      }
   }
}
