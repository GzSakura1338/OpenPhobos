package org.phobos.core.gui;

import java.awt.Color;
import java.util.function.Consumer;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.render.RenderManager;

public class ScrollState {
   public final FloatAnimator floatAnimator;
   public boolean flag;
   public float floatValue;
   public Consumer<Boolean> booleanValueConsumer;
   public boolean flag2;
   public float floatValue2;
   public String text;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();
   public final FloatAnimator floatAnimator3;
   public static final Color color = new Color(25, 25, 25, 100);

   public void run() {
      this.flag2 = !this.flag2;
      if (this.booleanValueConsumer != null) {
         this.booleanValueConsumer.accept(this.flag2);
      }
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }

   public void setText(String text) {
      this.text = text;
   }

   public FloatAnimator getFloatAnimator2() {
      return this.floatAnimator2;
   }

   public ScrollState() {
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator3 = new FloatAnimator();
      this.flag2 = false;
      this.flag = true;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public String getText() {
      return this.text;
   }

   public boolean testDouble(double d, double d2) {
      return this.flag && d2 >= this.floatValue2 - 2.0F && d2 <= this.floatValue2 + 8.4F && d >= this.floatValue - 2.0F && d <= this.floatValue + 16.0F;
   }

   public void setBooleanValueConsumer(Consumer<Boolean> consumer) {
      this.booleanValueConsumer = consumer;
   }

   public FloatAnimator getFloatAnimator3() {
      return this.floatAnimator3;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleRenderManager(
      RenderManager renderManager, float f, float f2, Color color, TriConsumer<@NotNull String, Float, Float> triConsumer
   ) {
      this.setFloatValue(f);
      this.setFloatValue2(f2);
      this.floatAnimator2.setFloatValue(this.flag2 ? 8.0F : 0.0F);
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
      this.floatAnimator.setFloatValue(this.flag2 ? 1.0F : 0.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.1F);
      Color color2 = ColorUtils.createColor8(ScrollState.color, ScrollState.color.brighter(), this.floatAnimator3.getFloatValue2());
      renderManager.getNanoVgRenderer().handleFloat2(f - 2.0F, f2 - 2.0F, 16.0F, 8.4F, 3.6F, color2);
      float f3 = this.floatAnimator.getFloatValue2();
      if (f3 > 0.05F) {
         renderManager.getNanoVgRenderer()
            .handleFloat3(
               f + 2.0F + this.floatAnimator2.getFloatValue2(),
               f2 + 2.0F,
               this.flag ? 8.0F : 6.0F,
               (this.flag ? 0.4F : 0.2F) * f3,
               ColorUtils.createColor6(color, f3)
            );
      }

      Color color3 = ColorUtils.createColor8(color2, color, f3);
      renderManager.getNanoVgRenderer().handleFloat(f + 2.0F + this.floatAnimator2.getFloatValue2(), f2 + 2.0F, 4.0F, !this.flag ? color3.darker() : color3);
      if (this.text != null) {
         triConsumer.accept(this.text, f + 19.0F, f2 - 0.5F);
      }
   }

   public Consumer<Boolean> getBooleanValueConsumer() {
      return this.booleanValueConsumer;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }
}
