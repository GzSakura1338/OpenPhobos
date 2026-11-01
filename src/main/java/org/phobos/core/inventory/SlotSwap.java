package org.phobos.core.inventory;

import net.minecraft.screen.slot.Slot;

public class SlotSwap {
   public final Slot slot;
   public final Slot slot2;

   public SlotSwap(Slot slot, Slot slot2) {
      this.slot2 = slot;
      this.slot = slot2;
   }

   public Slot getSlot() {
      return this.slot;
   }

   public Slot getSlot2() {
      return this.slot2;
   }
}
