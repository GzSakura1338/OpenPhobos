package org.phobos.core.event.events;

public class PlayerMoveEvent extends CancellableEvent {
   public double doubleValue;
   public boolean flag;
   public float floatValue;
   public float floatValue2;
   public double doubleValue2;
   public double doubleValue3;

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public double getDoubleValue() {
      return this.doubleValue2;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public double getDoubleValue2() {
      return this.doubleValue3;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public PlayerMoveEvent(double d, double d2, double d3, boolean flag, float f, float f2) {
      this.doubleValue3 = d;
      this.doubleValue = d2;
      this.doubleValue2 = d3;
      this.flag = flag;
      this.floatValue = f;
      this.floatValue2 = f2;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue3() {
      return this.doubleValue;
   }
}
