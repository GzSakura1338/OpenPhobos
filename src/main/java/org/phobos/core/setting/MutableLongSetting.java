package org.phobos.core.setting;

import org.jetbrains.annotations.NotNull;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.element.MutableLongElement;

public class MutableLongSetting extends MutableNumberSetting<Long> {
   public final boolean flag;

   public MutableLongSetting(String text, NumberRange.Mutable<Long> mutable, long l, long l2, boolean flag) {
      super(text, mutable, l, l2);
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public MutableLongSetting(String text, NumberRange.Mutable<Long> mutable, long l, long l2) {
      super(text, mutable, l, l2);
      this.flag = l < 1000L;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new MutableLongElement(
         guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this, this.getNumber2(), this.getNumber3()
      );
   }

   public MutableLongSetting(String text, long l, long l2, long l3) {
      super(text, l, l2, l3);
      this.flag = l2 < 1000L;
   }

   public MutableLongSetting(String text, long l, long l2, long l3, boolean flag) {
      super(text, l, l2, l3);
      this.flag = flag;
   }

   public MutableLongSetting(String text, NumberRange.Mutable<Long> mutable, long l, long l2, long l3, boolean flag) {
      super(text, mutable, l, l2, l3);
      this.flag = flag;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      String[] texts2 = texts[0].split("-");
      if (texts2.length != 2) {
         return false;
      } else {
         Long longValue = TimeParser.resolveLongValue(texts2[0]);
         Long longValue2 = TimeParser.resolveLongValue(texts2[1]);
         if (longValue != null && longValue2 != null) {
            this.setValue(NumberRange.createMutable(longValue, longValue2));
            return true;
         } else {
            return false;
         }
      }
   }
}
