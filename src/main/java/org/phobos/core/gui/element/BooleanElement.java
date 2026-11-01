package org.phobos.core.gui.element;

import java.awt.Color;
import org.phobos.api.Easing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class BooleanElement extends SettingElement<Boolean, BooleanSetting> {
   public boolean flag;
   public static final Logger LOGGER = LoggerFactory.getLogger(BooleanElement.class);
   public final EasedAnimation easedAnimation = new EasedAnimation(350L);

   public BooleanElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      BooleanSetting booleanSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, booleanSetting);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      if (this.easedAnimation.isFlag() != this.getSetting().getValue()) {
         this.easedAnimation.handleFlag2(this.getSetting().getValue());
      }

      float f = this.easedAnimation.computeEasing(Easing.InOutEasing.INSTANCE3);
      this.handleDouble12(
         this.getFloatValue3() * f,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         ColorUtils.createColor5(this.getGuiManager().getUiTheme().getColor12(), this.isFlag7() ? 15 : 0)
      );
      if (f > 0.05) {
         this.handleDouble11(
            0.0,
            0.0,
            this.getFloatValue3() * f,
            this.getFloatValue4(),
            this.getGuiManager().getUiTheme().getColor4(),
            this.getGuiManager().getUiTheme().getUIShader()
         );
      }

      String text = this.getText3();
      this.handleText2(text, 2.0F, 3.0F, Color.WHITE, true);
   }

   public EasedAnimation getEasedAnimation() {
      return this.easedAnimation;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      if (i == 0 && this.isFlag7()) {
         this.setValue(!this.getValue());
         this.run15();
      }
   }
}
