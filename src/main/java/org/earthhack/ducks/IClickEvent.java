package org.earthhack.ducks;

import java.util.List;
import net.minecraft.text.ClickEvent;

public interface IClickEvent {
   default void addCallback(Runnable runnable) {
      this.getCallbacks().add(runnable);
   }

   List<Runnable> getCallbacks();

   static IClickEvent cast(ClickEvent event) {
      return (IClickEvent)(Object)event;
   }
}
