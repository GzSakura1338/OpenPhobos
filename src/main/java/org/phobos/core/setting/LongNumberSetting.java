package org.phobos.core.setting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.element.LongNumberElement;
import org.phobos.core.gui.ModuleElement;

public class LongNumberSetting extends NumberSetting<Long> {
   public final boolean flag;

   @Override
   public boolean testString(@NotNull String... texts) {
      Long longValue = TimeParser.resolveLongValue(texts[0]);
      if (longValue == null) {
         return false;
      }

      this.setValue(longValue);
      return true;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new LongNumberElement(
         guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this, this.getNumber2(), this.getNumber3()
      );
   }

   public LongNumberSetting(String text, long l, long l2, long l3) {
      this(text, l, l2, l3, l2 < 1000L);
   }

   public LongNumberSetting(String text, @Nullable String text2, long l, long l2, long l3, boolean flag) {
      super(text, text2, l, l2, l3);
      this.flag = flag;
   }

   public LongNumberSetting(String text, long l, long l2, long l3, boolean flag) {
      this(text, "", l, l2, l3, flag);
   }

   public boolean isFlag() {
      return this.flag;
   }
}
