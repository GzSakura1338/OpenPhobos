package org.phobos.core.gui;

public abstract class NamedColorPicker extends ColorPicker {
   public final String name;

   public NamedColorPicker(String text) {
      this.name = text;
   }

   public String getName() {
      return this.name;
   }
}
