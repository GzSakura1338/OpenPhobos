package org.phobos.core.bind;

import org.phobos.api.Named;
import org.phobos.type.KeyboardMouseEnum;

public class BindKey implements Named {
   public final String name;
   public final int intValue;
   public final KeyboardMouseEnum keyboardMouseEnum;

   public KeyboardMouseEnum getKeyboardMouseEnum() {
      return this.keyboardMouseEnum;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public BindKey(String text, KeyboardMouseEnum keyboardMouseEnum, int i) {
      this.name = text;
      this.keyboardMouseEnum = keyboardMouseEnum;
      this.intValue = i;
   }

   @Override
   public String toString() {
      return "{" + this.name + ", " + this.keyboardMouseEnum + ", " + this.intValue + "}";
   }

   @Override
   public String getName() {
      return this.name;
   }
}
