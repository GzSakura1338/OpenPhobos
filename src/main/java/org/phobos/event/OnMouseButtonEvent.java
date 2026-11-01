package org.phobos.event;

import org.phobos.api.MinecraftHolder;
import org.phobos.core.bind.BindKey;
import org.phobos.core.PhobosClient;
import org.phobos.type.KeyboardMouseEnum;

public class OnMouseButtonEvent implements MinecraftHolder {
   public final int intValue;
   public final int intValue2;
   public final KeyboardMouseEnum keyboardMouseEnum;

   public BindKey resolveBindKey(PhobosClient phobosClient) {
      return phobosClient.getBindManager().resolveBindKey(this.keyboardMouseEnum, this.intValue2);
   }

   public OnMouseButtonEvent(int i, int i2, KeyboardMouseEnum keyboardMouseEnum) {
      this.intValue2 = i;
      this.intValue = i2;
      this.keyboardMouseEnum = keyboardMouseEnum;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public KeyboardMouseEnum getKeyboardMouseEnum() {
      return this.keyboardMouseEnum;
   }

   public boolean check() {
      return this.intValue == 1;
   }

   public boolean check2() {
      return this.intValue == 2;
   }

   public boolean check3() {
      return mc.currentScreen == null;
   }
}
