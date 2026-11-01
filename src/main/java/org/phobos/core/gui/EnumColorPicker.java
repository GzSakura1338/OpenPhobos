package org.phobos.core.gui;

import org.phobos.core.setting.EnumSetting;

public abstract class EnumColorPicker<T extends Enum<T>> extends ColorPicker {
   public T enumValue;
   public final String name;

   public String getName() {
      return this.name;
   }

   public void setEnumValue(T enumValue) {
      this.enumValue = enumValue;
   }

   public EnumColorPicker(String text, T enumValue) {
      this.name = text;
      this.enumValue = enumValue;
   }

   @Override
   public void run2() {
      this.enumValue = (T)EnumSetting.resolveEnumValue2(this.enumValue);
      this.run3();
   }

   public T getEnumValue() {
      return this.enumValue;
   }

   public abstract void run3();
}
