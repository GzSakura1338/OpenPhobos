package org.phobos.core.render;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ContextConsumer<T> extends ArrayList<Consumer<T>> {
   public void handleObject(T object, boolean flag) {
      if (!this.isEmpty()) {
         Consumer<T> consumer = this.getFirst();
         if (consumer != null) {
            consumer.accept(object);
            if (flag) {
               this.remove(consumer);
            }
         }
      }
   }

   public void handleObject2(T object, boolean flag) {
      if (flag) {
         for (Consumer<T> consumer : this) {
            consumer.accept(object);
         }
      }

      this.clear();
   }
}
