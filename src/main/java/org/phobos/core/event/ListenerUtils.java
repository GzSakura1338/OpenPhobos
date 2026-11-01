package org.phobos.core.event;

import java.util.function.Consumer;
import org.phobos.api.ListenerHolder;
import org.phobos.event.Listener;

public final class ListenerUtils {
   public static <S extends ListenerHolder, E, C extends Class<E>> void handleListenerHolder(S listenerHolder, C classValue, Consumer<E> consumer) {
      listenerHolder.getListenerList().add(new Listener<E>() {
         @Override
         public void handleObject2(E object) {
            consumer.accept(object);
         }
      });
   }

   public ListenerUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
