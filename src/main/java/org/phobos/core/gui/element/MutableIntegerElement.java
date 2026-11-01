package org.phobos.core.gui.element;

import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MutableNumberSetting;

public class MutableIntegerElement extends MutableNumberElement<Integer> {
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

         int i = Integer.parseInt(texts[0]);
         int i2 = Integer.parseInt(texts[1]);
         ((NumberRange.Mutable)this.getValue()).handleNumber(i, i2);
      } catch (Exception var4) {
      }
   }

   @Override
   public void handleChar(char c) {
      if (Character.isDigit(c) || c == '-') {
         if (c != '-' || this.number2 < 0 || !this.getText().trim().isEmpty()) {
            this.setText(this.getText() + c);
         }
      }
   }

   public MutableIntegerElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MutableNumberSetting<Integer> mutableNumberSetting,
      Integer integer,
      Integer integer2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, mutableNumberSetting, integer, integer2);
   }

   @Override
   public void handleDouble5(double d, double d2) {
      int i = this.number2 + (Integer)((NumberRange.Mutable)this.getValue()).getNumber2() + (Integer)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue2 = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber3(
            MathUtils.computeInt3(
               (int)(Math.floor(this.doubleValue2 / this.floatValue * Math.abs(this.number - this.number2)) + this.number2.intValue()), i, this.number
            )
         );
   }

   @Override
   public void handleDouble6(double d, double d2) {
      int i = (Integer)((NumberRange.Mutable)this.getValue()).getNumber3() - (Integer)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber2(
            MathUtils.computeInt3(
               (int)(Math.floor(this.doubleValue / this.floatValue * Math.abs(this.number - this.number2)) + this.number2.intValue()), this.number2, i
            )
         );
   }

   public Integer resolveNumber(String text, boolean flag) {
      return Integer.parseInt(text) * (flag ? -1 : 1);
   }
}
