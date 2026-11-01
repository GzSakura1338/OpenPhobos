package org.phobos.core.gui.element;

import java.awt.Color;
import java.math.RoundingMode;
import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.MutableNumberSetting;

public class MutableLongElement extends MutableNumberElement<Long> {
   public final MutableLongSetting mutableLongSetting;

   @Override
   public void handleDouble5(double d, double d2) {
      long l = this.number2 + (Long)((NumberRange.Mutable)this.getValue()).getNumber2() + (Long)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue2 = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber3(
            MathUtils.computeLong2(
               (long)(Math.floor(this.doubleValue2 / this.floatValue * Math.abs(this.number - this.number2)) + this.number2.longValue()), l, this.number
            )
         );
   }

   @Override
   public void run() {
      if (!this.getText().isEmpty()) {
         this.setText(this.getText().substring(0, this.getText().length() - 1));
      }
   }

   @Override
   public void handleChar(char c) {
      if (Character.isDigit(c) || c == '-') {
         if (c != '-' || this.number2 < 0L || !this.getText().trim().isEmpty()) {
            this.setText(this.getText() + c);
         }
      }
   }

   public Long resolveNumber(String text, boolean flag) {
      return Long.parseLong(text) * (flag ? -1 : 1);
   }

   @Override
   public void handleDouble6(double d, double d2) {
      long l = (Long)((NumberRange.Mutable)this.getValue()).getNumber3() - (Long)((MutableNumberSetting)this.getSetting()).getNumber();
      this.doubleValue = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue);
      ((NumberRange.Mutable)this.getValue())
         .setNumber2(
            MathUtils.computeLong2(
               (long)(Math.floor(this.doubleValue / this.floatValue * Math.abs(this.number - this.number2)) + this.number2.longValue()), this.number2, l
            )
         );
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.run6();
      this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), this.color);
      NumberRange.Mutable<Long> mutable = (NumberRange.Mutable<Long>)this.getValue();
      float f = this.floatAnimator.getFloatValue2() * (this.getFloatValue3() + 1.0F);
      float f2 = this.floatAnimator2.getFloatValue2() * (this.getFloatValue3() + 1.0F);
      this.handleDouble11(
         f,
         0.0,
         MathUtils.computeFloat3(f2, 0.5F, this.getFloatValue3()),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor6(),
         this.getGuiManager().getUiTheme().getUIShader3()
      );
      if (this.flag) {
         this.handleText2(this.text + this.describe(), 2.0F, 3.0F, Color.WHITE, true);
      } else {
         String text = this.getText3();
         this.handleText2(text, 2.0F, 3.0F, Color.WHITE, true);
         long l = mutable.getNumber2();
         long l2 = mutable.getNumber3();
         double d3 = l / 1000.0;
         double d4 = l2 / 1000.0;
         long l3 = 1000L;
         String text2 = this.mutableLongSetting.isFlag() && l < l3
            ? l + "ms"
            : (l % 1000L == 0L ? Math.round(d3) + "s" : MathUtils.computeDouble2(d3, 1, RoundingMode.FLOOR) + "s");
         String text3 = this.mutableLongSetting.isFlag() && l2 < l3
            ? l2 + "ms"
            : (l2 % 1000L == 0L ? Math.round(d4) + "s" : MathUtils.computeDouble2(d4, 1, RoundingMode.FLOOR) + "s");
         String text4 = String.join("-", text2, text3);
         this.handleText2(text4, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text4)), 3.0F, Color.WHITE, true);
      }
   }

   public MutableLongElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MutableLongSetting mutableLongSetting,
      Long longValue,
      Long longValue2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, mutableLongSetting, longValue, longValue2);
      this.mutableLongSetting = mutableLongSetting;
   }
}
