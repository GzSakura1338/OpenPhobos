package org.phobos.core.gui.element;

import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MutableNumberSetting;

public class MutableFloatElement extends MutableNumberElement<Float> {
   @Override
   public void run() {
      if (!this.getText().isEmpty()) {
         this.setText(this.getText().substring(0, this.getText().length() - 1));
      }
   }

   @Override
   public void handleChar(char c) {
      if (Character.isDigit(c) || c == '.' || c == '-') {
         if (c != '-' || !(this.number2 >= 0.0F) || !this.getText().trim().isEmpty()) {
            this.setText(this.getText() + c);
         }
      }
   }

   @Override
   public void handleDouble6(double d, double d2) {
      float f = (Float)((NumberRange.Mutable)this.getValue()).getNumber3() - (Float)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber2(
            MathUtils.computeFloat3(
               (float)MathUtils.computeDouble(this.doubleValue / this.floatValue * Math.abs(this.number - this.number2), 2) + this.number2, this.number2, f
            )
         );
   }

   @Override
   public void handleDouble5(double d, double d2) {
      float f = this.number2 + (Float)((NumberRange.Mutable)this.getValue()).getNumber2() + (Float)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue2 = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber3(
            MathUtils.computeFloat3(
               (float)MathUtils.computeDouble(this.doubleValue2 / this.floatValue * Math.abs(this.number - this.number2), 2) + this.number2, f, this.number
            )
         );
   }

   public MutableFloatElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MutableNumberSetting<Float> mutableNumberSetting,
      Float floatValue,
      Float floatValue2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, mutableNumberSetting, floatValue, floatValue2);
   }

   public Float resolveNumber(String text, boolean flag) {
      return Float.parseFloat(text) * (flag ? -1 : 1);
   }

   @Override
   public void run2() {
      this.setFlag(false);

      try {
         String[] texts = this.getText().split("-");
         if (texts.length != 2) {
            return;
         }

         float f = Float.parseFloat(texts[0]);
         float f2 = Float.parseFloat(texts[1]);
         ((NumberRange.Mutable)this.getValue()).handleNumber(f, f2);
      } catch (Exception var4) {
      }
   }
}
