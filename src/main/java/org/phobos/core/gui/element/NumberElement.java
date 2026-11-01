package org.phobos.core.gui.element;

import java.awt.Color;
import org.phobos.api.DragCapture;
import org.phobos.api.TimeGate;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.NumberSetting;

public abstract class NumberElement<N extends Number> extends SettingElement<N, NumberSetting<N>> implements DragCapture {
   public final TimeGate.Normal normal;
   public boolean flag;
   public String text;
   public boolean flag2;
   public boolean flag3;
   public float floatValue;
   public double doubleValue;
   public final FloatAnimator floatAnimator;
   public final N number;
   public final N number2;
   public float floatValue2 = 0.0F;
   public final Color color;

   @Override
   public void handleChar2(char c, int i) {
      if (this.flag) {
         this.handleChar(c);
      }
   }

   public abstract void handleChar(char var1);

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

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public abstract void run2();

   public void run7() {
      this.text = "";
   }

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

   public NumberElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      NumberSetting<N> numberSetting,
      N number,
      N number2
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, numberSetting);
      this.normal = new TimeGate.Normal();
      this.floatAnimator = new FloatAnimator();
      this.color = new Color(0, 0, 0, 70);
      this.number2 = number;
      this.number = number2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public FloatAnimator getFloatAnimator4() {
      return this.floatAnimator;
   }

   public void setText(String text) {
      this.text = text;
   }

   public abstract void run();

   public abstract void handleDouble5(double var1, double var3);

   public Color getColor() {
      return this.color;
   }

   @Override
   public CursorUtils.Style getStyle() {
      return this.flag2 ? CursorUtils.Style.Resize_Sideways : CursorUtils.Style.Default;
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), this.color);
      NumberSetting<N> numberSetting = this.getSetting();
      this.floatAnimator
         .setFloatValue((numberSetting.getValue().floatValue() - this.number2.floatValue()) / (this.number.floatValue() - this.number2.floatValue()));
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
         String text2 = this.getText3();
         this.handleText2(text2, 2.0F, 3.0F, Color.WHITE, true);
         if (numberSetting.check()) {
            String text = numberSetting.describeNumber(numberSetting.getValue());
            if (text != null) {
               this.handleText2(text, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text)), 3.0F, Color.WHITE, true);
               return;
            }
         }

         String text3 = (
               !(numberSetting.getValue() instanceof Float) && !(numberSetting.getValue() instanceof Double)
                  ? this.getValue().toString()
                  : MathUtils.computeDouble(numberSetting.getValue().doubleValue(), 2)
            )
            + numberSetting.getText2();
         this.handleText2(text3, (float)(this.getFloatValue3() - 2.0F - this.computeText5(text3)), 3.0F, Color.WHITE, true);
      }
   }

   public String describe() {
      if (this.normal.testLong2(500L)) {
         this.flag3 = !this.flag3;
      }

      return this.flag3 ? "_" : "";
   }

   @Override
   public void handleDouble4(double d, double d2) {
      boolean flag = this.isFlag7();
      if (this.getGuiGraphics().testInt(0)) {
         if (this.flag) {
            this.run2();
         }

         if (flag && this.getGuiManager().getDragCapture() == null) {
            this.floatValue2 = this.getFloatValue3();
            this.floatValue = this.getFloatValue2();
            this.flag2 = true;
            this.getGuiManager().setDragCapture(this);
         }
      } else if (this.getGuiManager().getDragCapture() != null && this.getGuiManager().getDragCapture().equals(this)) {
         this.flag2 = false;
         this.getGuiManager().setDragCapture(null);
      } else {
         this.flag2 = false;
      }

      if (this.flag2) {
         this.handleDouble5(d, d2);
      }
   }

   public String getText() {
      return this.text;
   }

   public double computeDouble(double d, int i) {
      return Math.round(d * Math.pow(10.0, i)) / Math.pow(10.0, i);
   }
}
