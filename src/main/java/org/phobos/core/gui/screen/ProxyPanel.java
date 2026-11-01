package org.phobos.core.gui.screen;

import org.phobos.api.TimeGate;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.proxy.ProxyManager;

public class ProxyPanel extends GuiPanel {
   public final boolean flag;
   public final TimeGate.Normal normal;
   public final FloatAnimator floatAnimator = new FloatAnimator();
   public final FloatAnimator floatAnimator2 = new FloatAnimator(1.0F, 1.0F);

   @Override
   public void run5() {
      this.run();
      super.run5();
   }

   public ProxyPanel(ProxyGuiManager proxyGuiManager, String text, boolean flag) {
      super(proxyGuiManager, text, 0.0F, 0.0F, 216.0F, 15.0F);
      this.normal = new TimeGate.Normal();
      this.flag = flag;
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      super.handleInt2(i, i2, i3);
      if (this.flag && i == 294 && this.normal.testLong2(1000L)) {
         ProxyManager.run9();
         this.run();
      }
   }

   @Override
   public void run() {
      this.floatAnimator.setFloatValue2(0.0F);
      this.floatAnimator2.setFloatValue2(1.0F);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      super.handleDouble2(d, d2);
      if (this.flag) {
         this.floatAnimator2.setFloatValue(this.floatAnimator.getFloatValue2() >= 0.95F ? 0.0F : 1.0F);
         this.floatAnimator2.handleFloat3(10.0F, 0.02F);
         float f = 1.0F;
         this.floatAnimator.setFloatValue(f);
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
         this.handleDouble11(
            0.0,
            this.getFloatValue4(),
            this.getFloatValue3() * this.floatAnimator.getFloatValue2(),
            this.getFloatValue4() + 2.0F,
            ColorUtils.createColor6(this.getGuiManager().getUiTheme().getColor6(), this.floatAnimator2.getFloatValue2()),
            this.getGuiManager().getUiTheme().getUIShader3()
         );
      }
   }

   public void handleGuiElement(GuiElement guiElement) {
      this.getGuiElementList2().add(guiElement);
      this.resolveGuiElement3(guiElement);
   }
}
