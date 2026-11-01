package org.phobos.event;

import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.event.events.CancellableEvent;

public class InitEvent<T extends Screen> extends CancellableEvent {
   @Nullable
   public final T screen;

   @Nullable
   public T getScreen() {
      return this.screen;
   }

   public InitEvent(@Nullable T screen) {
      this.screen = screen;
   }
}
