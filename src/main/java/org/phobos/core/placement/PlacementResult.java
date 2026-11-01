package org.phobos.core.placement;

public class PlacementResult {
   public final boolean flag;
   public final int intValue;

   public boolean isFlag() {
      return this.flag;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public PlacementResult(boolean flag, int i) {
      this.flag = flag;
      this.intValue = i;
   }
}
