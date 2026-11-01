package org.phobos.event;

import net.minecraft.client.gui.DrawContext;

public class RenderEvent2 {
   public final float floatValue;
   public final DrawContext drawContext;

   public DrawContext getDrawContext() {
      return this.drawContext;
   }

   public RenderEvent2(DrawContext drawContext, float f) {
      this.drawContext = drawContext;
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
