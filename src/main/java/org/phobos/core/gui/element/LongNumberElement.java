package org.phobos.core.gui.element;

import java.awt.Color;
import java.math.RoundingMode;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.NumberSetting;

public class LongNumberElement extends NumberElement<Long> {
   public final LongNumberSetting longNumberSetting;

   @Override
   public void run2() {
      this.setFlag(false);

      try {
         long l = Long.parseLong(this.getText());
         this.setValue(((NumberSetting)this.getSetting()).isFlag2() ? l : MathUtils.computeLong2(l, this.number2, this.number));
      } catch (Exception exception) {
         if (!((NumberSetting)this.getSetting()).testText(this.getText())) {
            this.setText("");
            this.setValue((Long)((NumberSetting)this.getSetting()).getObject());
         }
      }
   }

   public LongNumberElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      LongNumberSetting longNumberSetting,
      Long longValue,
      Long longValue2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, longNumberSetting, longValue, longValue2);
      this.longNumberSetting = longNumberSetting;
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), this.getColor());
      this.floatAnimator.setFloatValue((this.getValue().floatValue() - this.number2.floatValue()) / (this.number.floatValue() - this.number2.floatValue()));
      this.floatAnimator.handleFloat3(0.16F, 0.06F);
      float f = this.floatAnimator.getFloatValue2() * (this.getFloatValue3() + 1.0F);
      this.handleDouble11(
         0.0,
         0.0,
         MathUtils.computeFloat3(f, 0.5F, this.getFloatValue3()),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor6(),
         this.getGuiManager().getUiTheme().getUIShader3()
      );
      if (this.flag) {
         this.handleText2(this.text + this.describe(), 2.0F, 3.0F, Color.WHITE, true);
      } else {
         if (this.longNumberSetting.check()) {
            String text = this.longNumberSetting.describeNumber(this.getValue());
            if (text != null) {
               this.handleText2(this.getText3(), 2.0F, 3.0F, Color.WHITE, true);
               this.handleText2(text, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text)), 3.0F, Color.WHITE, true);
               return;
            }
         }

         long l = this.getValue();
         double d3 = l / 1000.0;
         long l2 = 1000L;
         String text2 = this.longNumberSetting.isFlag() && l < l2
            ? l + "ms"
            : (this.getValue() % 1000L == 0L ? Math.round(d3) + "s" : MathUtils.computeDouble2(d3, 1, RoundingMode.FLOOR) + "s");
         this.handleText2(this.getText3(), 2.0F, 3.0F, Color.WHITE, true);
         this.handleText2(text2, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text2)), 3.0F, Color.WHITE, true);
      }
   }

   @Override
   public void handleDouble5(double d, double d2) {
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue, 0.0, this.floatValue2);
      this.setValue((long)(Math.floor(this.doubleValue / this.floatValue2 * Math.abs(this.number - this.number2)) + this.number2.longValue()));
   }

   @Override
   public void run() {
      if (!this.getText().isEmpty()) {
         this.setText(this.getText().substring(0, this.getText().length() - 1));
      }
   }

   @Override
   public void handleChar(char c) {
      if (!((NumberSetting)this.getSetting()).check2()) {
         if (!Character.isDigit(c) && c != '.' && c != '-') {
            return;
         }

         if (c == '-' && this.number2 >= 0L) {
            return;
         }
      }

      this.setText(this.getText() + c);
   }
}
