package org.phobos.core.render;

import java.awt.Color;
import java.util.Random;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.api.MinecraftHolder;

public class ColoredRect implements MinecraftHolder {
   public final Color color = new Color(-1714829883);
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public Color getColor() {
      return this.color;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public ColoredRect(float f, float f2, float f3, float f4) {
      this.floatValue2 = f;
      this.floatValue = f2;
      this.floatValue4 = f3;
      this.floatValue3 = f4;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public void handleMatrixStack(MatrixStack matrixStack) {
      RenderUtils.handleMatrixStack29(matrixStack, this.color, this.getFloatValue2(), this.getFloatValue(), this.floatValue3, this.floatValue3, 360);
      this.setFloatValue(this.getFloatValue() + this.floatValue4);
      if (this.getFloatValue() > mc.getWindow().getScaledHeight() + 10 || this.getFloatValue() < -10.0F) {
         this.setFloatValue(-10.0F);
         Random random = new Random();
         this.floatValue4 = random.nextFloat(2.0F) + 0.1F;
         this.floatValue3 = random.nextFloat(2.0F) + 0.1F;
      }
   }
}
