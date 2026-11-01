package org.phobos.api;

import org.phobos.event.Listener;

public interface ListenerAdapter<E> {
   default Listener<E> createValue() {
      return new Listener<E>() {
         @Override
         public void handleObject2(E object) {
            ListenerAdapter.this.handleObject3(object);
         }
      };
   }

   void handleObject3(E var1);
}
