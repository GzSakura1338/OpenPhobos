package org.phobos.core.hud;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopCenterEnum;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;

public abstract class AnimatedListHudElement<T> extends ListHudElement<T> {
   public int intValue;
   public final FloatAnimator floatAnimator;
   public float floatValue9;
   public boolean flag;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();
   public boolean flag9;

   public FloatAnimator getFloatAnimator9() {
      return this.floatAnimator;
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public void handleDrawContext(DrawContext drawContext) {
      this.floatAnimator2.setFloatValue(this.value4());
      this.floatAnimator2.handleFloat3(10.0F, this.getFloatValue22());
      this.floatAnimator.handleFloat3(10.0F, this.getFloatValue22());
      this.dataTypeList.removeIf(dataTypex -> dataTypex.flag && dataTypex.getFloatAnimator3().getFloatValue2() < 0.01F);
      float f7 = (!this.topCenterEnum.equals(TopCenterEnum.Top) ? -10 : 0) + (this.flag ? this.floatAnimator.getFloatValue2() : 0.0F);
      float f8 = f7;
      float f9 = 0.0F;
      float[] floats = values2(this.getFloatValue2() - 2.0F, this.getFloatValue(), this.getFloatValue3() + 4.0F, this.getFloatValue4());
      this.handleDouble5(floats[0], floats[1], floats[2] - floats[0], floats[3] - floats[1]);
      this.setFlag8(false);

      for (ListHudElement.DataType<T> dataType : this.dataTypeList) {
         ListHudElement.ReservedString reservedString = dataType.getDataTypeBiFunction().apply(dataType, dataType.getObject());
         float f = this.computeText2(reservedString.getText2());
         float f2 = dataType.flag ? 0.0F : f;
         dataType.floatAnimator2.setFloatValue(f2);
         dataType.floatAnimator2.handleFloat3(5.0F, dataType.flag ? 0.2F : 0.1F);
         float f3 = dataType.floatAnimator2.getFloatValue2();
         f9 = Math.max(f9, f2);
         dataType.floatAnimator.setFloatValue(this.computeDataType3(dataType));
         dataType.floatAnimator3.setFloatValue(dataType.floatAnimator.getFloatValue2() < 0.5F && dataType.flag ? 0.0F : 10.0F);
         dataType.floatAnimator3.handleFloat3(10.0F, 0.1F);
         dataType.floatAnimator.handleFloat3(10.0F, this.computeDataType(dataType));
         if (dataType.getFloatAnimator().getFloatValue2() < 0.1F) {
            f8 += this.computeFloat(dataType.floatAnimator3.getFloatValue2());
         } else {
            label52: {
               float f4 = this.check2() ? this.getFloatValue2() - f3 : this.getFloatValue2() - f + f3;
               float f5 = this.getFloatValue() + f8;
               float f6 = dataType.floatAnimator.getFloatValue2();
               String text = reservedString.getText();
               if (dataType.function != null) {
                  Color color = dataType.function.apply(dataType.object);
                  if (color != null) {
                     this.handleDrawContext8(drawContext, text, f4, f5, ColorUtils.createColor6(color, f6));
                     break label52;
                  }
               }

               this.handleDrawContext7(drawContext, text, f4, f5, f6);
            }

            f8 += this.computeFloat(dataType.floatAnimator3.getFloatValue2());
         }
      }

      this.setFlag8(true);
      this.run3();
      this.setFloatValue4(-30.0F);
      this.setFloatValue3(f9 * (this.centerRightEnum.equals(CenterRightEnum.Right) ? -1 : 1));
   }

   public abstract float getFloatValue22();

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public float value4() {
      return this.flag9 ? 1.0F : 0.0F;
   }

   public float getFloatValue43() {
      return this.floatValue9;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public AnimatedListHudElement(String text) {
      super(text);
      this.floatAnimator = new FloatAnimator();
   }

   public int getIntValue() {
      return this.intValue;
   }

   public FloatAnimator getFloatAnimator2() {
      return this.floatAnimator2;
   }

   public void setFlag9(boolean flag) {
      this.flag9 = flag;
   }

   public void setFloatValue9(float f) {
      this.floatValue9 = f;
   }

   public boolean isFlag9() {
      return this.flag9;
   }

   public float computeFloat2(float f, float f2) {
      return f * f2;
   }
}
