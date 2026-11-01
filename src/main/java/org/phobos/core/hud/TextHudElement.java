package org.phobos.core.hud;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.setting.Setting;

public abstract class TextHudElement extends HudElement {
   public final Setting<Boolean> smoothing;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();

   public TextHudElement(String text) {
      super(text, 0.0F, 0.0F, 0.0F, 9.0F);
      this.smoothing = this.resolveSetting2(new BooleanSetting("Smoothing", true));
   }

   @Override
   public float[] values() {
      return values2(this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.getFloatValue4());
   }

   public float computeFloat(float f) {
      this.floatAnimator2.setFloatValue(f);
      this.floatAnimator2.handleFloat3(5.0F, 0.1F);
      return this.floatAnimator2.getFloatValue2();
   }

   public void handleDrawContext(DrawContext drawContext, String text, float f) {
      this.setFlag8(false);
      float f2 = this.computeText2(text);
      float f3 = this.computeFloat(f2);
      float f4 = this.check2() ? this.getFloatValue2() - f3 : this.getFloatValue2();
      this.handleDrawContext7(drawContext, text, f4, this.getFloatValue(), f);
      this.setFlag8(true);
      this.setFloatValue3(f3 * (this.check2() ? -1 : 1));
   }

   public void handleDrawContext9(DrawContext drawContext, String text) {
      this.setFlag8(false);
      float f = this.computeText2(text);
      float f2 = this.computeFloat(f);
      float f3 = this.smoothing.getValue()
         ? (this.check2() ? this.getFloatValue2() - f2 : this.getFloatValue2() - f + f2)
         : (this.check2() ? this.getFloatValue2() - f : this.getFloatValue2());
      this.handleDrawContext4(drawContext, text, f3, this.getFloatValue());
      this.setFlag8(true);
      this.setFloatValue3((this.smoothing.getValue() ? f2 : f) * (this.check2() ? -1 : 1));
   }
}
