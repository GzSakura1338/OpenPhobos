package org.phobos.core.gui.screen;

import org.phobos.api.TimeGate;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.script.ScriptManager;

public class ScriptPanel extends GuiPanel {
   public final TimeGate.Normal normal;
   public final FloatAnimator floatAnimator;
   public final boolean flag;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();

   @Override
   public void handleDouble2(double d, double d2) {
      super.handleDouble2(d, d2);
      if (this.flag) {
         this.floatAnimator.setFloatValue(this.floatAnimator2.getFloatValue2() >= 0.95F ? 0.0F : 1.0F);
         this.floatAnimator.handleFloat3(10.0F, 0.02F);
         float f = 1.0F;
         this.floatAnimator2.setFloatValue(f);
         this.floatAnimator2.handleFloat3(10.0F, 0.1F);
         this.handleDouble11(
            0.0,
            this.getFloatValue4(),
            this.getFloatValue3() * this.floatAnimator2.getFloatValue2(),
            this.getFloatValue4() + 2.0F,
            ColorUtils.createColor6(this.getGuiManager().getUiTheme().getColor6(), this.floatAnimator.getFloatValue2()),
            this.getGuiManager().getUiTheme().getUIShader3()
         );
      }
   }

   @Override
   public void run5() {
      this.run();
      super.run5();
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      super.handleInt2(i, i2, i3);
      if (this.flag && i == 294 && this.normal.testLong2(1000L)) {
         ScriptManager.run9();
         this.run();
      }
   }

   @Override
   public void run() {
      this.floatAnimator2.setFloatValue2(0.0F);
      this.floatAnimator.setFloatValue2(1.0F);
   }

   public void handleGuiElement(GuiElement guiElement) {
      this.getGuiElementList2().add(guiElement);
      this.resolveGuiElement3(guiElement);
   }

   public ScriptPanel(ScriptGuiManager scriptGuiManager, String text, boolean flag) {
      super(scriptGuiManager, text, 0.0F, 0.0F, 216.0F, 15.0F);
      this.floatAnimator = new FloatAnimator(1.0F, 1.0F);
      this.normal = new TimeGate.Normal();
      this.flag = flag;
   }
}
