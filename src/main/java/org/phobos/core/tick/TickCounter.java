package org.phobos.core.tick;

public class TickCounter {
   public int intValue;
   public static final TickCounter INSTANCE = new TickCounter();

   public int getIntValue() {
      return this.intValue;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }
}
