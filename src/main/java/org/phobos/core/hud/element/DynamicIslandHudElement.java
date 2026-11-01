package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.hud.HudElement;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.Setting;

public class DynamicIslandHudElement extends HudElement {
   public final Setting<Float> offset = this.resolveSetting2(new NumberSetting<>("Offset", 25.0F, 2.0F, 200.0F));
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2;
   public DynamicIslandHudElement.State state = DynamicIslandHudElement.State.Default;

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (this.state == null) {
         this.state = DynamicIslandHudElement.State.Default;
      }

      this.floatAnimator.run6();
      this.floatAnimator2.run6();
      float f = i / 2.0F;
      this.state.render(drawContext, this, f, this.offset.getValue());
   }

   public DynamicIslandHudElement() {
      super("DynamicIsland", 0.0F, 0.0F, 50.0F, 20.0F);
      this.floatAnimator = new FloatAnimator(50.0F);
      this.floatAnimator2 = new FloatAnimator(20.0F);
   }

   public static void handleDynamicIslandHudElement(DynamicIslandHudElement dynamicIslandHudElement, DrawContext drawContext, String text, float f, float f2) {
      dynamicIslandHudElement.handleDrawContext4(drawContext, text, f, f2);
   }

   public static float computeDynamicIslandHudElement(DynamicIslandHudElement dynamicIslandHudElement, String text) {
      return dynamicIslandHudElement.computeText2(text);
   }

   public static void handleDynamicIslandHudElement2(DynamicIslandHudElement dynamicIslandHudElement, Runnable runnable) {
      dynamicIslandHudElement.handleRunnable(runnable);
   }

   public static float computeDynamicIslandHudElement2(DynamicIslandHudElement dynamicIslandHudElement, String text) {
      return dynamicIslandHudElement.computeText(text);
   }

   public enum State {
      Default {
         @Override
         public float[] render(DrawContext ctx, DynamicIslandHudElement element, float center, float offset) {
            String data = "3arthh4ck";
            float width = DynamicIslandHudElement.computeDynamicIslandHudElement(element, data);
            float height = DynamicIslandHudElement.computeDynamicIslandHudElement2(element, data);
            DynamicIslandHudElement.handleDynamicIslandHudElement2(
               element, () -> DynamicIslandHudElement.handleDynamicIslandHudElement(element, ctx, data, center - width / 2.0F, offset)
            );
            return new float[]{width, height};
         }
      };

      public abstract float[] render(DrawContext var1, DynamicIslandHudElement var2, float var3, float var4);
   }
}
