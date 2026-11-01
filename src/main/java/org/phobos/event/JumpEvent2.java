package org.phobos.event;

public class JumpEvent2 {
   public float floatValue;

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public JumpEvent2(float f) {
      this.floatValue = f;
   }

   public static class Post {
   }
}
