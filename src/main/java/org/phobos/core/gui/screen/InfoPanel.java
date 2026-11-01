package org.phobos.core.gui.screen;

import org.phobos.api.TimeGate;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiPanel;

public class InfoPanel extends GuiPanel {
   public final TimeGate.Normal normal;
   public final FloatAnimator floatAnimator = new FloatAnimator();
   public final FloatAnimator floatAnimator2 = new FloatAnimator(1.0F, 1.0F);

   public FloatAnimator getFloatAnimator3() {
      return this.floatAnimator2;
   }

   public InfoPanel(InfoGuiManager infoGuiManager, String text) {
      super(infoGuiManager, text, 0.0F, 0.0F, 216.0F, 15.0F);
      this.normal = new TimeGate.Normal();
   }

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public FloatAnimator getFloatAnimator2() {
      return this.floatAnimator;
   }
}
