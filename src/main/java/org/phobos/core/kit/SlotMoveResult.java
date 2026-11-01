package org.phobos.core.kit;

import net.minecraft.screen.slot.Slot;

public class SlotMoveResult {
   public final boolean empty;
   public final Slot slot;
   public final Slot slot2;

   public Slot getSlot2() {
      return this.slot2;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public SlotMoveResult(Slot slot, Slot slot2, boolean flag) {
      this.slot = slot;
      this.slot2 = slot2;
      this.empty = flag;
   }

   public Slot getSlot() {
      return this.slot;
   }
}
