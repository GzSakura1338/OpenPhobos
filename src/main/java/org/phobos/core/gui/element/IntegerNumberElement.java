package org.phobos.core.gui.element;

import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.NumberSetting;

public class IntegerNumberElement extends NumberElement<Integer> {
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

         if (c == '-' && this.number2 >= 0) {
            return;
         }
      }

      this.setText(this.getText() + c);
   }

   public IntegerNumberElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      NumberSetting<Integer> numberSetting,
      Integer integer,
      Integer integer2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, numberSetting, integer, integer2);
   }

   @Override
   public void run2() {
      this.setFlag(false);

      try {
         int i = Integer.parseInt(this.getText());
         this.setValue(((NumberSetting)this.getSetting()).isFlag2() ? i : MathUtils.computeInt3(i, this.number2, this.number));
      } catch (Exception exception) {
         if (!((NumberSetting)this.getSetting()).testText(this.getText())) {
            this.setText("");
            this.setValue((Integer)((NumberSetting)this.getSetting()).getObject());
         }
      }
   }

   @Override
   public void handleDouble5(double d, double d2) {
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue, 0.0, this.floatValue2);
      this.setValue((int)(Math.floor(this.doubleValue / this.floatValue2 * Math.abs(this.number - this.number2)) + this.number2.intValue()));
   }
}
