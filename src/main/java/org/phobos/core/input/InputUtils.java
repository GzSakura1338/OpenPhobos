package org.phobos.core.input;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.slot.Slot;
import org.phobos.api.MinecraftHolder;

public final class InputUtils implements MinecraftHolder {
   public static void handleSlot(Slot slot) {
      try {
         handleKeyBinding(mc.options.hotbarKeys[slot.getIndex()]);
      } catch (Throwable var2) {
      }
   }

   public static void handleKeyBinding(KeyBinding keyBinding) {
      keyBinding.timesPressed = 1;
   }

   public static void handleInt(int i) {
      if (i >= 1 && i <= 9) {
         handleKeyBinding(mc.options.hotbarKeys[i - 1]);
      }
   }

   public InputUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
