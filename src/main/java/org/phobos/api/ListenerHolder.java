package org.phobos.api;

import java.util.List;
import java.util.function.Consumer;
import org.phobos.event.Listener;

public interface ListenerHolder {
   default <E> void handleConsumer4(Consumer<E> consumer) {
      this.handleListener(new Listener<E>() {
         @Override
         public void handleObject2(E object) {
            consumer.accept(object);
         }
      });
   }

   default void handleListenerHolder(ListenerHolder listenerHolder) {
      this.getListenerList().addAll(listenerHolder.getListenerList());
   }

   List<Listener<?>> getListenerList();

   default void handleListener(Listener<?> listener) {
      this.getListenerList().add(listener);
   }
}
