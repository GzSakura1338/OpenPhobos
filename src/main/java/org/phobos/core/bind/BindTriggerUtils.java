package org.phobos.core.bind;

import java.util.function.Supplier;
import org.phobos.api.ListenerHolder;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.TickEvent2;
import org.phobos.core.ClientHolder;

public class BindTriggerUtils implements MinecraftHolder {
   public static void handleSupplier(Supplier<KeyCombination> supplier, Runnable runnableArg, ListenerHolder listenerHolder, BindTriggerUtils.Type type) {
      switch (type) {
         case Press:
            listenerHolder.getListenerList().add(new Listener<OnMouseButtonEvent>() {
               public void handleObject2(OnMouseButtonEvent onMouseButtonEvent) {
                  if (onMouseButtonEvent.check()) {
                     if (MinecraftHolder.mc.currentScreen == null) {
                        KeyCombination keyCombination = supplier.get();
                        BindKey bindKey = onMouseButtonEvent.resolveBindKey(ClientHolder.getPhobosClient());
                        if (bindKey != null && keyCombination.getBindKeySet().contains(bindKey)) {
                           runnableArg.run();
                        }
                     }
                  }
               }
            });
            break;
         case Active:
            listenerHolder.getListenerList().add(new Listener<TickEvent2>() {
               public void handleObject2(TickEvent2 tickEvent2) {
                  KeyCombination keyCombination = supplier.get();
                  if (MinecraftHolder.mc.currentScreen == null) {
                     if (ClientHolder.getPhobosClient().getBindManager().testKeyCombination(keyCombination)) {
                        runnableArg.run();
                     }
                  }
               }
            });
      }
   }

   public enum Type {
      Press,
      Active;
   }
}
