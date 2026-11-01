package org.phobos.core.gui.element;

import java.awt.Color;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.SelectionManager;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.phobos.api.TimeGate;
import org.phobos.module.client.UIModule;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.HoverRegion;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.ModuleSearch;
import org.phobos.core.setting.StringSetting;

public class StringElement extends SettingValueElement<String, StringSetting> {
   public final StringElement.CurrentString currentString;
   public boolean flag;
   @HashCodeExclude
   public final Color color;
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public String text2;
   public boolean flag2;

   public StringElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      StringSetting stringSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, stringSetting);
      this.color = new Color(0, 0, 0, 70);
      this.currentString = new StringElement.CurrentString(stringSetting.getValue());
      this.text2 = stringSetting.getValue();
   }

   @Override
   public CursorUtils.Style getStyle() {
      return CursorUtils.Style.Text;
   }

   public String describeText(String text) {
      String text2 = "";
      if (text != null && !text.isEmpty()) {
         text2 = text.substring(0, text.length() - 1);
      }

      return text2;
   }

   public Color getColor() {
      return this.color;
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

   public String getText2() {
      return this.text2;
   }

   @Override
   public void handleChar2(char c, int i) {
      if (this.flag2) {
         if (!this.testChar2(c)) {
            return;
         }

         if (this.currentString.getString().length() >= this.getSetting().getLimit()) {
            return;
         }

         this.handleText6(this.currentString.getString() + c);
      }
   }

   public boolean isFlag() {
      return this.flag2;
   }

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public StringElement.CurrentString getCurrentString() {
      return this.currentString;
   }

   public boolean isFlag2() {
      return this.flag;
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      if (this.flag2) {
         if (i == 257) {
            this.run2();
            this.flag2 = false;
            return;
         }

         if (i == 259) {
            if (Screen.hasControlDown()) {
               this.handleText6(this.describeText2(this.currentString.getString()));
            } else {
               this.handleText6(this.describeText(this.currentString.getString()));
            }

            return;
         }

         if (i != 264 && i != 335 && Screen.isPaste(i)) {
            this.handleText6(ChatTextUtils.describeText5(this.currentString.getString() + this.describe2(), this.getSetting().getLimit()));
         }
      }
   }

   public void run2() {
      if (this.currentString.getString().isEmpty()) {
         this.setValue("");
         this.setText("");
      } else {
         this.setValue(this.currentString.getString());
         this.setText(this.currentString.getString());
      }
   }

   public String describeFlag(boolean flag) {
      return flag ? UIModule.getUIModule().open.getValue() : UIModule.getUIModule().close.getValue();
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.getFloatValue4() + 2.0F : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
   }

   public boolean testChar(char c) {
      Predicate<Character> predicate = this.getSetting().getPacketPredicate();
      return predicate != null ? predicate.test(c) : true;
   }

   public void run9() {
      this.flag2 = !this.flag2;
   }

   public void setText(String text) {
      this.text2 = text;
   }

   public boolean testChar2(char c) {
      return c != 167 && c >= ' ' && c != 127 && this.testChar(c);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor5(),
         this.getGuiManager().getUiTheme().getUIShader4()
      );
      String text2 = this.getText3();
      this.handleText2(text2, 2.0F, 3.0F, Color.WHITE, true);
      String text3 = this.describeFlag(!this.isFlag10());
      this.handleText2(text3, (int)(this.getFloatValue3() - this.computeText5(text3) - 2.0), 3.0F, Color.WHITE, true);
      if (this.getFloatValue13().getFloatValue2() > 0.05F) {
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         this.handleDouble12(
            2.0, this.getFloatValue4() + 2.0F, this.getFloatValue3() - 2.0F, this.getFloatValue4() + 2.0F + this.getFloatValue13().getFloatValue2(), this.color
         );
         String text = this.text2 + (this.flag2 ? this.describe() : "");
         this.handleText2(
            this.flag2 ? text : this.getValue(),
            4.0F,
            this.getFloatValue4() + 5.0F,
            Color.WHITE,
            true,
            this.testDouble2(
               d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + this.getFloatValue4() + 2.0F, this.getFloatValue3() - 2.0F, this.getFloatValue4()
            )
         );
         this.run3();
      }
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (!this.isFlag10()) {
               if (this.flag2) {
                  this.flag2 = false;
                  this.run2();
               }

               return;
            }

            if (this.testDouble2(
               d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + this.getFloatValue4() + 2.0F, this.getFloatValue3(), this.getFloatValue4()
            )) {
               this.run15();
               this.run9();
               return;
            }
            break;
         case 1:
            if (this.isFlag7()) {
               this.setFlag10(!this.isFlag10());
               this.run14();
            }

            if (!this.isFlag10() && this.flag2) {
               this.flag2 = false;
               this.run2();
            }
      }
   }

   public void handleText6(String text) {
      this.currentString.setString(text);
      this.setText(text);
      if (Objects.equals(this.getSetting(), ModuleSearch.getSearch())) {
         this.run2();
      }
   }

   public String describe() {
      if (this.normal.testLong2(500L)) {
         this.flag = !this.flag;
      }

      return this.flag ? "_" : "";
   }

   @Override
   public HoverRegion getHoverRegion() {
      return this.isFlag10()
         ? HoverRegion.createHoverRegion(this.floatValue12, this.floatValue10 + this.floatValue11 + 2.0F, this.floatValue6, this.floatValue11)
         : HoverRegion.getINSTANCE();
   }

   public String describeText2(String text) {
      if (text != null && !text.isEmpty()) {
         int i = text.lastIndexOf(32);
         return i == -1 ? "" : text.substring(0, i);
      } else {
         return text;
      }
   }

   private static class CurrentString {
      public String string;

      public String getString() {
         return this.string;
      }

      public void setString(String text) {
         this.string = text;
      }

      public CurrentString(String text) {
         this.string = text;
      }
   }
}
