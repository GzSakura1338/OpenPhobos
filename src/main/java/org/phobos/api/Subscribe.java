package org.phobos.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.phobos.core.event.SubscriberInvoker;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
   int priority() default 10;

   boolean direct() default false;

   interface Processor {
      static <T> SubscriberInvoker<T> createSubscriberInvoker(T object) {
         return new SubscriberInvoker<>(object, object.getClass());
      }

      static void handleListenerHolder(ListenerHolder listenerHolder) {
         createSubscriberInvoker(listenerHolder)
            .resolveObject(SubscriberInvoker::run)
            .resolveObject(subscriberInvoker -> listenerHolder.getListenerList().addAll(subscriberInvoker.getListenerList()));
      }
   }
}
