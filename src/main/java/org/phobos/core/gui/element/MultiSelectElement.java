package org.phobos.core.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextProvider;
import org.phobos.module.client.UIModule;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.HoverRegion;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.MultiSelectSetting;

public class MultiSelectElement<T> extends SettingValueElement<Map<T, Boolean>, MultiSelectSetting<T>> {
   public float floatValue;
   public final List<MultiSelectElement.Entry<T>> entryList = new ArrayList<>();

   public void handleEntry(MultiSelectElement.Entry<T> entry, float f, float f2) {
      entry.setFloatValue2(this.getFloatValue2() + f2);
      entry.setFloatValue4(this.getFloatValue() + this.getFloatValue4() + f);
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? Math.max(this.floatValue, this.getFloatValue4() + 2.0F) : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
   }

   public void handleEntry2(MultiSelectElement.Entry<T> entry, double d, double d2) {
      this.run10();
      entry.floatAnimator.setFloatValue(this.getValue().getOrDefault(entry.object, false) ? 1.0F : 0.0F);
      entry.floatAnimator.handleFloat3(10.0F, 0.2F);
      boolean flag = this.testDouble2(d, d2, entry.getFloatValue2(), entry.getFloatValue4(), entry.getFloatValue3(), entry.getFloatValue());
      if (flag) {
         if (entry.getText() != null) {
            this.getGuiManager().handleText(entry.getText(), entry);
         }
      } else {
         this.getGuiManager().handleObject(this);
      }

      this.handleText2(entry.name, entry.getFloatValue2() + 2.0F, entry.getFloatValue4() + 3.0F, Color.WHITE, true, flag);
      Color color = this.getGuiManager().getUiTheme().getColor12();
      Color color2 = this.getGuiManager().getUiTheme().getColor4();
      this.handleDouble12(
         entry.getFloatValue2(),
         entry.getFloatValue4(),
         entry.getFloatValue2() + entry.getFloatValue3(),
         entry.getFloatValue4() + entry.getFloatValue(),
         ColorUtils.createColor8(color, color2, entry.floatAnimator.getFloatValue2())
      );
      this.run13();
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (!this.isFlag10() || this.isFlag7()) {
               return;
            }

            for (MultiSelectElement.Entry<T> entry : this.entryList) {
               if (entry.testDouble(d, d2)) {
                  T object = entry.object;
                  this.getValue().put(object, !this.getValue().getOrDefault(object, true));
                  this.getSetting().run();
                  this.run15();
                  return;
               }
            }
            break;
         case 1:
            if (!this.isFlag7()) {
               return;
            }

            this.setFlag10(!this.isFlag10());
            this.run14();
      }
   }

   public MultiSelectElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      MultiSelectSetting<T> multiSelectSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, multiSelectSetting);
      this.floatValue = 0.0F;

      for (Map.Entry<T, String> entry : this.getSetting().getMap().entrySet()) {
         T object = entry.getKey();
         this.entryList.add(new MultiSelectElement.Entry<>(entry.getValue().replaceAll("_", " "), TextProvider.describeObject2(object), object));
      }
   }

   @Override
   public HoverRegion getHoverRegion() {
      return HoverRegion.createHoverRegion(this.getFloatValue2(), this.getFloatValue() + this.getFloatValue4(), this.getFloatValue3(), this.floatValue);
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
      String text2 = this.describeFlag(!this.isFlag10());
      this.handleText2(text2, (int)(this.getFloatValue3() - this.computeText5(text2) - 2.0), 3.0F, Color.WHITE, true);
      if (this.getFloatValue13().getFloatValue2() > 0.05F) {
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         float f = 2.0F;
         float f2 = 2.0F;
         float f3 = 0.0F;

         for (MultiSelectElement.Entry<T> entry : this.entryList) {
            entry.setFloatValue3((float)(this.computeText5(entry.name) + 4.0));
            entry.setFloatValue((float)(this.computeText(entry.name) + 4.0));
            if (f3 == 0.0F) {
               f3 = entry.getFloatValue() + 2.0F;
            }

            if (f + entry.getFloatValue3() + 2.0F >= this.getFloatValue3()) {
               f2 += f3;
               f = 2.0F;
            }

            this.handleEntry(entry, f2, f);
            this.handleEntry2(entry, d, d2);
            f += entry.getFloatValue3() + 2.0F;
         }

         this.floatValue = f2 + f3 - 2.0F;
         this.run3();
      }
   }

   @Override
   public CursorUtils.Style getStyle() {
      return this.isFlag10() ? CursorUtils.Style.Link : CursorUtils.Style.Default;
   }

   public String describeFlag(boolean flag) {
      return flag ? UIModule.getUIModule().open.getValue() : UIModule.getUIModule().close.getValue();
   }

   private static class Entry<T> {
      @Nullable
      public final String text;
      public float floatValue;
      public float floatValue2;
      public boolean flag;
      public float floatValue3;
      public float floatValue4;
      public final T object;
      public final FloatAnimator floatAnimator = new FloatAnimator();
      public final String name;

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public Entry(String text, @Nullable String text2, T object) {
         this.name = text;
         this.text = text2;
         this.object = object;
      }

      public void setFloatValue4(float f) {
         this.floatValue4 = f;
      }

      @Nullable
      public String getText() {
         return this.text;
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public T getObject() {
         return this.object;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public String getName() {
         return this.name;
      }

      public boolean testDouble(double d, double d2) {
         return d >= this.floatValue2 && d <= this.floatValue2 + this.floatValue3 && d2 >= this.floatValue4 && d2 <= this.floatValue4 + this.floatValue;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public float getFloatValue4() {
         return this.floatValue4;
      }

      @Override
      public String toString() {
         return "Entry(" + this.name + ", " + this.object + ")";
      }
   }
}
