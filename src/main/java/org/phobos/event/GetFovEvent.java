package org.phobos.event;

public class GetFovEvent {
   public double doubleValue;

   public GetFovEvent(double d) {
      this.doubleValue = d;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }
}
