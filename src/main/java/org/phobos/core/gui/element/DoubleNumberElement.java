package org.phobos.core.gui.element;

import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.NumberSetting;

public class DoubleNumberElement extends NumberElement<Double> {
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
         float f = Float.parseFloat(this.getText());
         this.setValue(((NumberSetting)this.getSetting()).isFlag2() ? f : MathUtils.computeDouble5(f, this.number2, this.number));
      } catch (Exception exception) {
         if (!((NumberSetting)this.getSetting()).testText(this.getText())) {
            this.setText("");
            this.setValue((Double)((NumberSetting)this.getSetting()).getObject());
         }
      }
   }

   @Override
   public void handleDouble5(double d, double d2) {
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue, 0.0, this.floatValue2);
      this.setValue(MathUtils.computeDouble(this.doubleValue / this.floatValue2 * Math.abs(this.number - this.number2) + this.number2, 2));
   }

   @Override
   public void handleChar(char c) {
      if (!((NumberSetting)this.getSetting()).check2()) {
         if (!Character.isDigit(c) && c != '.' && c != '-') {
            return;
         }

         if (c == '-' && this.number2 >= 0.0) {
            return;
         }
      }

      this.setText(this.getText() + c);
   }

   public DoubleNumberElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      NumberSetting<Double> numberSetting,
      double d,
      double d2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, numberSetting, d, d2);
   }
}
