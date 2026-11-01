package org.phobos.event;

import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.phobos.core.event.events.CancellableEvent;

public class OnMouseClickEvent extends CancellableEvent {
   public final SlotActionType slotActionType;
   public final int intValue;
   public final Slot slot;
   public final int intValue2;

   public int getIntValue() {
      return this.intValue;
   }

   public Slot getSlot() {
      return this.slot;
   }

   public SlotActionType getSlotActionType() {
      return this.slotActionType;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public OnMouseClickEvent(Slot slot, int i, int i2, SlotActionType slotActionType) {
      this.slot = slot;
      this.intValue2 = i;
      this.intValue = i2;
      this.slotActionType = slotActionType;
   }
}
