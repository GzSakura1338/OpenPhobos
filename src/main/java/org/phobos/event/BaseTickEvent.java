package org.phobos.event;

public class BaseTickEvent {
   public int intValue;

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public BaseTickEvent(int i) {
      this.intValue = i;
   }
}
