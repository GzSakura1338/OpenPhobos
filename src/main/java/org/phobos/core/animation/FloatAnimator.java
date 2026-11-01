package org.phobos.core.animation;

import java.util.function.Consumer;
import org.phobos.api.ListenerHolder;
import org.phobos.api.AnimationValue;
import org.phobos.api.AnimationValue;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.ListenerUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.RenderContext;
import org.phobos.core.setting.Setting;
import org.phobos.core.tick.TimerManager;

public class FloatAnimator implements MinecraftHolder, AnimationValue.Float {
   public float floatValue;
   public float floatValue2;

   @Override
   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public void handleFloat2(float f) {
      this.setFloatValue(this.floatValue + f);
   }

   public FloatAnimator() {
      this(0.0F);
   }

   public <S extends Object & ListenerHolder, E extends RenderContext> void handleObject4(S object, float f, float f2) {
      this.handleObject2(object, f, f2, renderContext -> {});
   }

   public <S extends Object & ListenerHolder, E extends RenderContext, F extends Setting<Float>> void handleObject3(
      S object, F setting, F setting2, Consumer<RenderContext> consumer
   ) {
      ListenerUtils.handleListenerHolder(object, RenderContext.class, renderContext -> {
         consumer.accept(renderContext);
         this.handleFloat3(setting2.getValue(), setting.getValue());
      });
   }

   public void run6() {
      this.handleFloat3(1.0F, 0.02F);
   }

   public <S extends Object & ListenerHolder, E extends RenderContext, F extends Setting<Float>> void handleObject(S object, F setting, F setting2) {
      this.handleObject3(object, setting, setting2, renderContext -> {});
   }

   @Override
   public float getFloatValue2() {
      return this.floatValue2;
   }

   @Override
   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   @Override
   public void handleFloat3(float f, float f2) {
      float f3 = TimerManager.value() * 6.7F;
      double d = this.getFloatValue() > this.getFloatValue2() ? 1.0 : -1.0;
      double d2 = Math.abs(this.getFloatValue() - this.getFloatValue2());
      double d3 = Math.min(f, d2 * f2);
      double d4 = this.getFloatValue2() + d * d3 * f3;
      double d5 = d == 1.0 ? Math.min(d4, this.getFloatValue()) : Math.max(d4, this.getFloatValue());
      this.setFloatValue2((float)d5);
   }

   public boolean testFloat(float f) {
      return MathUtils.testFloat(this.getFloatValue2(), this.getFloatValue(), f);
   }

   public <S extends Object & ListenerHolder, E extends RenderContext> void handleObject2(S object, float f, float f2, Consumer<RenderContext> consumer) {
      ListenerUtils.handleListenerHolder(object, RenderContext.class, renderContext -> {
         consumer.accept(renderContext);
         this.handleFloat3(f2, f);
      });
   }

   public boolean isFinished() {
      return Math.round(this.getFloatValue2()) == Math.round(this.getFloatValue());
   }

   public void run() {
      this.handleFloat3(5.0F, 0.1F);
   }

   public FloatAnimator(float f, float f2) {
      this.floatValue2 = f;
      this.floatValue = f2;
   }

   @Override
   public void run2() {
      this.setFloatValue2(0.0F);
      this.setFloatValue(0.0F);
   }

   public void handleFloat(float f) {
      this.setFloatValue2(f);
      this.setFloatValue(f);
   }

   @Override
   public float getFloatValue() {
      return this.floatValue;
   }

   public FloatAnimator(float f) {
      this(f, f);
   }

   public static class Double implements MinecraftHolder, AnimationValue.Double {
      public double doubleValue;
      public double doubleValue2;

      public void run6() {
         this.handleDouble(1.0, 0.02F);
      }

      @Override
      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public boolean isFinished() {
         return Math.round(this.getDoubleValue()) == Math.round(this.getDoubleValue2());
      }

      @Override
      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void handleFloat(float f) {
         this.setDoubleValue(f);
         this.setDoubleValue2(f);
      }

      public Double(float f) {
         this(f, f);
      }

      public void handleFloat2(float f) {
         this.setDoubleValue2(this.doubleValue2 + f);
      }

      public <S extends Object & ListenerHolder, E extends RenderContext, F extends Setting<Float>> void handleObject(S object, F setting, F setting2) {
         this.handleObject3(object, setting, setting2, renderContext -> {});
      }

      @Override
      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public Double(float f, float f2) {
         this.doubleValue = f;
         this.doubleValue2 = f2;
      }

      public void run() {
         this.handleDouble(5.0, 0.1F);
      }

      public boolean testFloat(float f) {
         return MathUtils.testDouble(this.getDoubleValue(), this.getDoubleValue2(), f);
      }

      public <S extends Object & ListenerHolder, E extends RenderContext> void handleObject2(S object, float f, float f2, Consumer<RenderContext> consumer) {
         ListenerUtils.handleListenerHolder(object, RenderContext.class, renderContext -> {
            consumer.accept(renderContext);
            this.handleDouble(f2, f);
         });
      }

      @Override
      public void handleDouble(double d, double d2) {
         float f = TimerManager.value() * 6.7F;
         double d3 = this.getDoubleValue2() > this.getDoubleValue() ? 1.0 : -1.0;
         double d4 = Math.abs(this.getDoubleValue2() - this.getDoubleValue());
         double d5 = Math.min(d, d4 * d2);
         double d6 = this.getDoubleValue() + d3 * d5 * f;
         double d7 = d3 == 1.0 ? Math.min(d6, this.getDoubleValue2()) : Math.max(d6, this.getDoubleValue2());
         this.setDoubleValue(d7);
      }

      @Override
      public void run2() {
         this.setDoubleValue(0.0);
         this.setDoubleValue2(0.0);
      }

      public Double() {
         this(0.0F);
      }

      public <S extends Object & ListenerHolder, E extends RenderContext, F extends Setting<Float>> void handleObject3(
         S object, F setting, F setting2, Consumer<RenderContext> consumer
      ) {
         ListenerUtils.handleListenerHolder(object, RenderContext.class, renderContext -> {
            consumer.accept(renderContext);
            this.handleDouble(setting2.getValue().floatValue(), setting.getValue().floatValue());
         });
      }

      @Override
      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public <S extends Object & ListenerHolder, E extends RenderContext> void handleObject4(S object, float f, float f2) {
         this.handleObject2(object, f, f2, renderContext -> {});
      }
   }
}
