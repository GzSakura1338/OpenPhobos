package org.phobos.event;

import net.minecraft.client.input.KeyboardInput;
import org.phobos.core.event.events.CancellableEvent;

public class TickEvent extends CancellableEvent {
   public final KeyboardInput keyboardInput;

   public KeyboardInput getKeyboardInput() {
      return this.keyboardInput;
   }

   public TickEvent(KeyboardInput keyboardInput) {
      this.keyboardInput = keyboardInput;
   }
}
