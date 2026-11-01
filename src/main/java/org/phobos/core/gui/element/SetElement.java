package org.phobos.core.gui.element;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.phobos.api.TimeGate;
import org.phobos.module.client.UIModule;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.SetSetting;

public class SetElement<T> extends SettingValueElement<Set<T>, SetSetting<T>> {
   @HashCodeExclude
   public final Color color;
   @HashCodeExclude
   public final Color color2;
   public final TimeGate.Normal normal;
   public final FloatAnimator floatAnimator = new FloatAnimator();
   public boolean flag;
   public String text2;
   public String text3;
   public boolean flag2;
   public final SetElement.CurrentString currentString;
   public final Set<T> set;
   @HashCodeExclude
   public final Color color3;
   public final Set<SetElement.DisplayItem<T>> displayItemSet;

   public void run2() {
      this.text3 = null;
      if (this.currentString.getString().isEmpty()) {
         this.setText("");
      } else {
         if (this.getSetting().check()) {
            List<T> list = this.getSetting().resolveList(this.currentString.getString());
            if (!list.isEmpty()) {
               for (T object : list) {
                  this.set.add(object);
                  this.displayItemSet
                     .add(
                        new SetElement.DisplayItem<>(
                           object, this.getSetting().getFunction().apply(object), this.getSetting().getFunction2().apply(object)
                        )
                     );
               }
            }

            this.setValue(this.set);
         }

         this.handleText6("");
      }
   }

   public float value2() {
      float f = 0.0F;

      for (T object : this.set) {
         f += this.getFloatValue4() + 2.0F;
      }

      return f;
   }

   public void handleText6(String text) {
      this.currentString.setString(text);
      this.setText(text);
   }

   @Override
   public CursorUtils.Style getStyle() {
      return CursorUtils.Style.Text;
   }

   public void setText(String text) {
      this.text2 = text;
   }

   public String describeText(String text) {
      String text2 = "";
      if (text != null && !text.isEmpty()) {
         text2 = text.substring(0, text.length() - 1);
      }

      return text2;
   }

   public void run9() {
      this.flag = !this.flag;
   }

   public boolean testChar2(char c) {
      return c != 167 && c >= ' ' && c != 127;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.isFlag7()) {
               this.run9();
               return;
            }

            if (!this.isFlag10()) {
               return;
            }

            this.displayItemSet.removeIf(displayItem -> {
               if (displayItem.testDouble(d, d2)) {
                  this.set.remove(displayItem.object);
                  this.setValue(this.set);
                  this.run15();
                  return true;
               } else {
                  return false;
               }
            });
            break;
         case 1:
            if (this.isFlag7() && !this.displayItemSet.isEmpty()) {
               this.setFlag10(!this.isFlag10());
               this.run14();
            }
      }
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      if (this.flag) {
         if (i == 257) {
            this.run2();
            this.flag = false;
            return;
         }

         if (i == 259) {
            if (Screen.hasControlDown()) {
               this.handleText6(this.describeText2(this.currentString.getString()));
            } else {
               this.handleText6(this.describeText(this.currentString.getString()));
            }
         }

         if (i != 264 && i != 335 && Screen.isPaste(i)) {
            this.handleText6(this.currentString.getString() + this.describe2());
         }

         if (i == 258 && this.text3 != null) {
            this.handleText6(this.text3);
         }

         this.text3 = this.getSetting().describeText(this.currentString.getString());
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
         ColorUtils.createColor8(
            this.getGuiManager().getUiTheme().getColor14(), this.getGuiManager().getUiTheme().getColor3(), this.floatAnimator.getFloatValue2()
         ),
         this.getGuiManager().getUiTheme().getUIShader9()
      );
      if (this.flag && this.text3 != null) {
         this.handleText2(this.text3, 2.0F, 3.0F, this.color2, false);
      }

      String text = this.text2 + (this.flag ? this.describe() : "");
      String text2 = this.getText3();
      this.handleText2(this.flag ? text : text2, 2.0F, 3.0F, Color.WHITE, true);
      if (!this.displayItemSet.isEmpty()) {
         this.handleText2(!this.isFlag10() ? "+" : "-", (int)(this.getFloatValue3() - 8.0F), 3.0F, Color.WHITE, true);
      }

      if (this.getFloatValue13().getFloatValue2() > 0.05F) {
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         float f = this.getFloatValue4() + 2.0F;
         float f2 = UIModule.getUIModule().height.getValue() * 0.8F;
         float f3 = 1.08F;

         for (SetElement.DisplayItem<?> displayItem : this.displayItemSet) {
            displayItem.setFloatValue(this.getFloatValue2() + 2.0F);
            displayItem.setFloatValue2(this.getFloatValue() + f);
            displayItem.setFloatValue4(this.getFloatValue3() - 2.0F);
            displayItem.setFloatValue3(this.getFloatValue4());
            displayItem.setFlag2(displayItem.testDouble(d, d2));
            displayItem.run();
            boolean flag = !displayItem.getItemStack().isEmpty();
            if (flag) {
               this.getMatrixStack().push();
               this.getMatrixStack().translate(displayItem.getFloatValue() + f3, displayItem.getFloatValue2() + f3, 0.0F);
               this.getMatrixStack().scale(f2, f2, 1.0F);
               this.getGuiGraphics().getDrawContext().drawItem(displayItem.getItemStack(), 0, 0);
               this.getMatrixStack().pop();
            }

            this.getGuiManager()
               .getGuiGraphics()
               .handleDouble12(
                  this.getFloatValue2() + 2.0F,
                  this.getFloatValue() + f,
                  this.getFloatValue2() + this.getFloatValue3(),
                  this.getFloatValue() + f + this.getFloatValue4(),
                  ColorUtils.createColor8(this.color3, this.color, displayItem.floatAnimator.getFloatValue2())
               );
            this.handleText2(displayItem.getName(), flag ? 20.0F : 4.0F, f + 3.0F, Color.WHITE, true, displayItem.isFlag2());
            f += this.getFloatValue4() + 2.0F;
         }

         this.run3();
      }
   }

   public String describe() {
      if (this.normal.testLong2(500L)) {
         this.flag2 = !this.flag2;
      }

      return this.flag2 ? "_" : "";
   }

   public String describeText2(String text) {
      if (text != null && !text.isEmpty()) {
         int i = text.lastIndexOf(32);
         return i == -1 ? "" : text.substring(0, i);
      } else {
         return text;
      }
   }

   public SetElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      SetSetting<T> setSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, setSetting);
      this.normal = new TimeGate.Normal();
      this.set = new HashSet<>();
      this.displayItemSet = new HashSet<>();
      this.color2 = new Color(255, 255, 255, 70);
      this.color = new Color(255, 0, 0, 40);
      this.color3 = new Color(0, 0, 0, 70);

      for (T object : setSetting.getValue()) {
         this.set.add(object);
         this.displayItemSet
            .add(
               new SetElement.DisplayItem<>(
                  object, this.getSetting().getFunction().apply(object), this.getSetting().getFunction2().apply(object)
               )
            );
      }

      this.currentString = new SetElement.CurrentString("");
      this.text2 = "";
   }

   @Override
   public void handleChar2(char c, int i) {
      if (this.flag) {
         if (!this.testChar2(c)) {
            return;
         }

         this.handleText6(this.currentString.getString() + c);
      }
   }

   public String describe2() {
      String text = SelectionManager.getClipboard(mc);
      StringBuilder stringBuilder = new StringBuilder();

      for (char c : text.toCharArray()) {
         if (this.testChar2(c)) {
            stringBuilder.append(c);
         }
      }

      return stringBuilder.toString();
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.value2() : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      this.floatAnimator.setFloatValue(this.flag ? 1.0F : 0.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.1F);
   }

   public static class CurrentString {
      public String string;

      public CurrentString(String text) {
         this.string = text;
      }

      public String getString() {
         return this.string;
      }

      public void setString(String text) {
         this.string = text;
      }
   }

   public static class DisplayItem<T> {
      public final ItemStack itemStack;
      public final String name;
      @HashCodeExclude
      public float floatValue;
      @HashCodeExclude
      public float floatValue2;
      @HashCodeExclude
      public FloatAnimator floatAnimator = new FloatAnimator();
      public final T object;
      @HashCodeExclude
      public boolean flag;
      @HashCodeExclude
      public boolean flag2;
      @HashCodeExclude
      public float floatValue3;
      @HashCodeExclude
      public float floatValue4;

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      public T getObject() {
         return this.object;
      }

      public float getFloatValue4() {
         return this.floatValue4;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public DisplayItem(T object, ItemStack itemStack, String text) {
         this.object = object;
         this.itemStack = itemStack;
         this.name = text;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setFloatAnimator(FloatAnimator floatAnimator) {
         this.floatAnimator = floatAnimator;
      }

      public String getName() {
         return this.name;
      }

      public void run() {
         this.floatAnimator.setFloatValue(this.flag2 ? 1.0F : 0.0F);
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void setFloatValue4(float f) {
         this.floatValue4 = f;
      }

      @Override
      public boolean equals(Object object) {
         return object instanceof SetElement.DisplayItem<?> displayItem ? this.object == displayItem.object : false;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean testDouble(double d, double d2) {
         return d >= this.floatValue && d <= this.floatValue + this.floatValue4 && d2 >= this.floatValue2 && d2 <= this.floatValue2 + this.floatValue3;
      }
   }
}
