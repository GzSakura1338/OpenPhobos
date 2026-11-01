package org.phobos.core.gui.element;

import java.awt.Color;
import org.phobos.api.DragCapture;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MutableNumberSetting;

public abstract class MutableNumberElement<N extends Number> extends SettingElement<NumberRange.Mutable<N>, MutableNumberSetting<N>> implements DragCapture {
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2;
   public boolean flag;
   public final Color color;
   public double doubleValue;
   public float floatValue = 0.0F;
   public MutableNumberElement.Drag drag = MutableNumberElement.Drag.None;
   public boolean flag2;
   public final N number;
   public final N number2;
   public String text;
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public float floatValue2;
   public double doubleValue2;

   @Override
   public void handleInt2(int i, int i2, int i3) {
      if (this.flag) {
         if (i == 257) {
            this.run2();
         }

         if (i == 259) {
            this.run();
         }
      }
   }

   public abstract void handleDouble6(double var1, double var3);

   public String describe() {
      if (this.normal.testLong2(500L)) {
         this.flag2 = !this.flag2;
      }

      return this.flag2 ? "_" : "";
   }

   @Override
   public void handleChar2(char c, int i) {
      if (this.flag) {
         this.handleChar(c);
      }
   }

   public void run6() {
      this.floatAnimator.setFloatValue(this.value2());
      this.floatAnimator.handleFloat3(0.16F, 0.06F);
      this.floatAnimator2.setFloatValue(this.value6());
      this.floatAnimator2.handleFloat3(0.16F, 0.06F);
   }

   public MutableNumberElement.Drag resolveDrag(double d) {
      double d2 = MathUtils.computeDouble5(d - this.floatValue2, 0.0, this.floatValue) / this.floatValue;
      double d3 = Math.abs(d2 - this.value2());
      double d4 = Math.abs(d2 - this.value6());
      return MathUtils.testDouble(d3, d4, 0.05) ? this.resolveDrag2(d2) : (d3 < d4 ? MutableNumberElement.Drag.Min : MutableNumberElement.Drag.Max);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), this.color);
      this.run6();
      NumberRange.Mutable<N> mutable = this.getValue();
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
         MutableNumberSetting<N> mutableNumberSetting = this.getSetting();
         boolean flag = mutableNumberSetting.getNumber2() instanceof Float || mutableNumberSetting.getNumber2() instanceof Double;
         String text2 = (flag ? MathUtils.computeDouble(mutable.getNumber2().doubleValue(), 2) : mutable.getNumber2()) + "";
         String text3 = (flag ? MathUtils.computeDouble(mutable.getNumber3().doubleValue(), 2) : mutable.getNumber3()) + this.getText2();
         String text4 = String.join("-", text2, text3);
         this.handleText2(text4, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text4)), 3.0F, Color.WHITE, true);
      }
   }

   public abstract N resolveNumber(String var1, boolean var2);

   public float value2() {
      return (this.getValue().getNumber2().floatValue() - this.number2.floatValue()) / (this.number.floatValue() - this.number2.floatValue());
   }

   public void setText(String text) {
      this.text = text;
   }

   public abstract void handleChar(char var1);

   public boolean isFlag() {
      return this.flag;
   }

   public String getText2() {
      return this.getSetting().getText2() != null ? this.getSetting().getText2() : "";
   }

   @Override
   public void handleDouble4(double d, double d2) {
      boolean flag = this.isFlag7();
      if (this.getGuiGraphics().testInt(0)) {
         if (this.flag) {
            this.run2();
         }

         if (flag && this.getGuiManager().getDragCapture() == null) {
            this.floatValue = this.getFloatValue3();
            this.floatValue2 = this.getFloatValue2();
            this.drag = this.resolveDrag(d);
            this.getGuiManager().setDragCapture(this);
         }
      } else if (this.getGuiManager().getDragCapture() != null && this.getGuiManager().getDragCapture().equals(this)) {
         this.drag = MutableNumberElement.Drag.None;
         this.getGuiManager().setDragCapture(null);
      } else {
         this.drag = MutableNumberElement.Drag.None;
      }

      if (this.drag != null) {
         switch (this.drag) {
            case Min:
               this.handleDouble6(d, d2);
               break;
            case Max:
               this.handleDouble5(d, d2);
         }
      }
   }

   public void run2() {
      this.setFlag(false);

      try {
         String[] texts = this.getText().split("-");
         switch (texts.length) {
            case 2:
               N number = this.resolveNumber(texts[0], false);
               N number2 = this.resolveNumber(texts[1], false);
               this.getValue().handleNumber(number, number2);
               break;
            case 3:
               N number3 = this.resolveNumber(texts[1], true);
               N number4 = this.resolveNumber(texts[2], false);
               this.getValue().handleNumber(number3, number4);
               break;
            case 4:
               N number5 = this.resolveNumber(texts[2], true);
               N number6 = this.resolveNumber(texts[3], true);
               this.getValue().handleNumber(number5, number6);
         }
      } catch (Exception var4) {
      }
   }

   public MutableNumberElement.Drag resolveDrag2(double d) {
      return d > this.value6() ? MutableNumberElement.Drag.Max : MutableNumberElement.Drag.Min;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.flag) {
               this.run2();
            }
            break;
         case 1:
            if (this.flag) {
               this.run2();
            }

            if (this.isFlag7()) {
               this.run7();
               this.flag = true;
            }
      }
   }

   public abstract void handleDouble5(double var1, double var3);

   public String getText() {
      return this.text;
   }

   public float value6() {
      return (this.getValue().getNumber3().floatValue() - this.number2.floatValue()) / (this.number.floatValue() - this.number2.floatValue());
   }

   public Color getColor() {
      return this.color;
   }

   public void run7() {
      this.text = "";
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public double computeDouble(double d, int i) {
      return Math.round(d * Math.pow(10.0, i)) / Math.pow(10.0, i);
   }

   public MutableNumberElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MutableNumberSetting<N> mutableNumberSetting,
      N number,
      N number2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, mutableNumberSetting);
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator2 = new FloatAnimator();
      this.color = new Color(0, 0, 0, 70);
      this.number2 = number;
      this.number = number2;
   }

   public abstract void run();

   public enum Drag {
      None,
      Min,
      Max;
   }
}
