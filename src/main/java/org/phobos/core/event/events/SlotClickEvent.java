package org.phobos.core.event.events;

import net.minecraft.screen.slot.SlotActionType;

public class SlotClickEvent extends CancellableEvent {
   public final int intValue;
   public final int intValue2;
   public final SlotActionType slotActionType;

   public SlotActionType getSlotActionType() {
      return this.slotActionType;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public SlotClickEvent(int i, int i2, SlotActionType slotActionType) {
      this.intValue2 = i;
      this.intValue = i2;
      this.slotActionType = slotActionType;
   }
}
