package org.phobos.core.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class EnumElement<T extends Enum<T>> extends SettingValueElement<T, EnumSetting<T>> {
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2;
   public final List<EnumElement.Entry<T>> entryList = new ArrayList<>();
   public boolean flag;
   public boolean flag2;

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? (this.getFloatValue4() + 2.0F) * this.entryList.size() : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
      this.floatAnimator.handleFloat3(10.0F, 0.1F);
   }

   public EnumElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      EnumSetting<T> enumSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, enumSetting);
      this.floatAnimator2 = new FloatAnimator();
      this.floatAnimator = new FloatAnimator();

      for (Enum<T> enumValue : (Enum[])enumSetting.getValue().getDeclaringClass().getEnumConstants()) {
         this.entryList
            .add(
               new EnumElement.Entry<>(
                  enumValue, Named.describeObject2(enumValue, enumValue.name().replace("_", " ")), TextProvider.describeObject2(enumValue)
               )
            );
      }
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor14(),
         this.getGuiManager().getUiTheme().getUIShader8()
      );
      String text = this.getText3();
      this.handleText2(text, 2.0F, 3.0F, Color.WHITE, true);
      this.floatAnimator.setFloatValue(this.getValue().ordinal() * (this.getFloatValue4() + 2.0F));
      this.handleDouble(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4());
      this.handleFloat(-this.floatAnimator.getFloatValue2());
      this.run3();
      if (this.getFloatValue13().getFloatValue2() > 0.05F) {
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         this.handleDouble12(
            2.0,
            this.floatAnimator2.getFloatValue2(),
            this.getFloatValue3() - 2.0F,
            this.floatAnimator2.getFloatValue2() + this.getFloatValue4(),
            this.getGuiManager().getUiTheme().getColor14()
         );
         float f = this.getFloatValue4() + 2.0F;

         for (EnumElement.Entry<T> entry : this.entryList) {
            if (this.getValue().equals(entry.enumValue)) {
               this.floatAnimator2.setFloatValue(f);
               if (this.floatAnimator2.getFloatValue2() == 0.0F) {
                  this.floatAnimator2.setFloatValue2(f);
               }
            }

            this.handleText2(
               entry.name,
               4.0F,
               f + 3.0F,
               Color.WHITE,
               true,
               this.testDouble2(d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + f, this.getFloatValue3() - 2.0F, this.getFloatValue4())
            );
            if (this.testDouble2(d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + f, this.getFloatValue3() - 2.0F, this.getFloatValue4())) {
               if (entry.getText() != null) {
                  this.getGuiManager().handleText(entry.getText(), entry);
               }
            } else {
               this.getGuiManager().handleObject(entry);
            }

            f += this.getFloatValue4() + 2.0F;
         }

         this.run3();
      } else {
         this.floatAnimator2.handleFloat(0.0F);
      }
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.isFlag7()) {
               this.getSetting().run3();
               this.run15();
               return;
            }

            if (!this.isFlag10()) {
               return;
            }

            float f = this.getFloatValue4() + 2.0F;

            for (EnumElement.Entry<T> entry : this.entryList) {
               if (this.testDouble2(d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + f, this.getFloatValue3() - 2.0F, this.getFloatValue4())) {
                  this.setValue((T)entry.enumValue);
                  this.run15();
                  return;
               }

               f += this.getFloatValue4() + 2.0F;
            }
            break;
         case 1:
            if (this.isFlag7()) {
               this.setFlag10(!this.isFlag10());
               this.run14();
               return;
            }
      }
   }

   public void handleFloat(float f) {
      float f2 = f;

      for (EnumElement.Entry<T> entry : this.entryList) {
         String text = entry.name;
         this.handleText2(text, (float)(this.getFloatValue3() - this.computeText5(text) - 2.0), 3.0F + f2, Color.WHITE, true);
         f2 += this.getFloatValue4() + 2.0F;
         if (f2 >= 20.0F) {
            break;
         }
      }
   }

   private static class Entry<T extends Enum<T>> {
      @Nullable
      public final String text;
      public final String name;
      public final Enum<T> enumValue;
      public boolean flag;
      public boolean flag2;

      public String getName() {
         return this.name;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public Entry(Enum<T> enumValue, String text, @Nullable String text2) {
         this.enumValue = enumValue;
         this.name = text;
         this.text = text2;
      }

      @Nullable
      public String getText() {
         return this.text;
      }

      public Enum<T> getEnumValue() {
         return this.enumValue;
      }

      public boolean isFlag2() {
         return this.flag2;
      }
   }
}
