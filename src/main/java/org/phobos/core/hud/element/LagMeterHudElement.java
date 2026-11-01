package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.math.MathUtils;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.TextHudElement;

public class LagMeterHudElement extends TextHudElement {
   public final Setting<Boolean> display;
   public final Setting<Integer> time = this.resolveSetting2(new NumberSetting<>("Time", 500, 10, 5000));
   public final FloatAnimator floatAnimator;

   public LagMeterHudElement() {
      super("LagOMeter");
      this.display = this.resolveSetting2(new BooleanSetting("Display").resolveSetting7("Forces display"));
      this.floatAnimator = new FloatAnimator();
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      long l = this.phobosClient.getServerDetector().value();
      this.floatAnimator.setFloatValue(!this.display.getValue() && l <= this.time.getValue().intValue() ? 0.0F : 1.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.15F);
      float f = this.floatAnimator.getFloatValue2();
      if (!(f < 0.05F)) {
         String text = "Server is not responding. (%ss)".formatted(MathUtils.computeDouble(l / 1000.0, 1));
         this.handleDrawContext(drawContext, text, f);
      }
   }
}
