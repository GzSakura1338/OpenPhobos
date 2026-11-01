package org.phobos.core.kit;

import net.minecraft.screen.slot.Slot;

public class SlotHandle {
   public final Slot slot;

   public SlotHandle(Slot slot) {
      this.slot = slot;
   }

   public Slot getSlot() {
      return this.slot;
   }
}
