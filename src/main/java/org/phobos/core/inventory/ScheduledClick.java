package org.phobos.core.inventory;

import net.minecraft.screen.slot.SlotActionType;

public class ScheduledClick {
   public final SlotActionType slotActionType;
   public boolean flag;
   public boolean flag2;
   public final int intValue;
   public long longValue;
   public int intValue2;

   public SlotActionType getSlotActionType() {
      return this.slotActionType;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public ScheduledClick(int i, SlotActionType slotActionType) {
      this.intValue = i;
      this.slotActionType = slotActionType;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public int getIntValue2() {
      return this.intValue2;
   }
}
