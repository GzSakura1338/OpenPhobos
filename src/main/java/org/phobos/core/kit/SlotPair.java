package org.phobos.core.kit;

import net.minecraft.screen.slot.Slot;

public class SlotPair {
   public final Slot slot;
   public final Slot slot2;

   public SlotPair(Slot slot, Slot slot2) {
      this.slot = slot;
      this.slot2 = slot2;
   }

   public Slot getSlot() {
      return this.slot;
   }

   public Slot getSlot2() {
      return this.slot2;
   }
}
