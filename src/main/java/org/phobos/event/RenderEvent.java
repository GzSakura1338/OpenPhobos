package org.phobos.event;

public class RenderEvent {
   public final float floatValue;

   public float getFloatValue() {
      return this.floatValue;
   }

   public RenderEvent(float f) {
      this.floatValue = f;
   }
}
