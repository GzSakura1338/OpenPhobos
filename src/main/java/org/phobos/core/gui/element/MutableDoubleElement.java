package org.phobos.core.gui.element;

import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MutableNumberSetting;

public class MutableDoubleElement extends MutableNumberElement<Double> {
   public MutableDoubleElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MutableNumberSetting<Double> mutableNumberSetting,
      Double doubleValue,
      Double doubleValue2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, mutableNumberSetting, doubleValue, doubleValue2);
   }

   @Override
   public void handleDouble6(double d, double d2) {
      double d3 = (Double)((NumberRange.Mutable)this.getValue()).getNumber3() - (Double)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber2(
            MathUtils.computeDouble5(
               MathUtils.computeDouble(this.doubleValue / this.floatValue * Math.abs(this.number - this.number2), 2) + this.number2, this.number2, d3
            )
         );
   }

   public Double resolveNumber(String text, boolean flag) {
      return Double.parseDouble(text) * (flag ? -1 : 1);
   }

   @Override
   public void run() {
      if (!this.getText().isEmpty()) {
         this.setText(this.getText().substring(0, this.getText().length() - 1));
      }
   }

   @Override
   public void run2() {
      this.setFlag(false);

      try {
         String[] texts = this.getText().split("-");
         if (texts.length != 2) {
            return;
         }

         double d = Double.parseDouble(texts[0]);
         double d2 = Double.parseDouble(texts[1]);
         ((NumberRange.Mutable)this.getValue()).handleNumber(d, d2);
      } catch (Exception var6) {
      }
   }

   @Override
   public void handleChar(char c) {
      if (Character.isDigit(c) || c == '.' || c == '-') {
         if (c != '-' || !(this.number2 >= 0.0) || !this.getText().trim().isEmpty()) {
            this.setText(this.getText() + c);
         }
      }
   }

   @Override
   public void handleDouble5(double d, double d2) {
      double d3 = this.number2 + (Double)((NumberRange.Mutable)this.getValue()).getNumber2() + (Double)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue2 = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber3(
            MathUtils.computeDouble5(
               MathUtils.computeDouble(this.doubleValue2 / this.floatValue * Math.abs(this.number - this.number2), 2) + this.number2, d3, this.number
            )
         );
   }
}
