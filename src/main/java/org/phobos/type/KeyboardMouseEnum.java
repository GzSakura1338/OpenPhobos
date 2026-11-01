package org.phobos.type;

import org.phobos.api.Named;

public enum KeyboardMouseEnum implements Named {
   Keyboard("Keyboard"),
   Mouse("Mouse");

   public final String name;

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static KeyboardMouseEnum fromString(String string) {
      return string.equals("Mouse") ? Mouse : Keyboard;
   }

   KeyboardMouseEnum(final String name) {
      this.name = name;
   }
}
