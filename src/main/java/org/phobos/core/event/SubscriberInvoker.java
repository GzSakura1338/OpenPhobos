package org.phobos.core.event;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Contract;
import org.phobos.api.Fluent;
import org.phobos.api.Subscribe;
import org.phobos.event.Listener;
import org.phobos.core.util.AccessProviderUtils;

public class SubscriberInvoker<T> implements Fluent<SubscriberInvoker<T>> {
   public final List<Listener<?>> listenerList = new ArrayList<>();
   public final T object;
   public final Class<?> classValue;

   public SubscriberInvoker<T> get() {
      return this;
   }

   public Class<?> getValueClassValue2() {
      return this.classValue;
   }

   @Contract("!null, _, !null, _, !null-> new")
   public <E> Listener<E> createValue(
      Class<E> classValue, Class<?> classValue2, SubscriberInvoker.MethodHandle methodHandle, int i, AccessProviderUtils.AccessProvider<?>... accessProviders
   ) {
      return new Listener<E>(classValue, classValue2, i) {
         @Override
         public void handleObject2(Object object) {
            methodHandle.handleObject(object, accessProviders);
         }
      };
   }

   public SubscriberInvoker(T object, Class<?> classValue) {
      this.object = object;
      this.classValue = classValue;
   }

   public void run() {
      for (Method method : this.classValue.getDeclaredMethods()) {
         if (method.isAnnotationPresent(Subscribe.class)) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            this.handleMethod(method, subscribe.priority(), subscribe.direct());
         }
      }
   }

   public T getObject() {
      return this.object;
   }

   public void handleMethod(Method method, int i, boolean flag) {
      if (method.getParameterCount() != 0) {
         Parameter[] parameters;
         Class<?> classValue4;
         Class<?> classValue;
         label30: {
            parameters = method.getParameters();
            Type[] types2 = method.getGenericParameterTypes();
            Parameter parameter = parameters[0];
            classValue4 = parameter.getType();
            if (types2[0] instanceof ParameterizedType parameterizedType2) {
               Type[] types = parameterizedType2.getActualTypeArguments();
               if (types.length == 1) {
                  Type type = types[0];
                  if (type instanceof ParameterizedType parameterizedType) {
                     classValue = (Class<?>)parameterizedType.getRawType();
                     break label30;
                  }

                  if (type instanceof Class<?> classValue3) {
                     classValue = classValue3;
                     break label30;
                  }
               }
            }

            classValue = null;
         }

         boolean flag2 = method.getParameterCount() == 1;
         SubscriberInvoker.MethodHandle methodHandle = new SubscriberInvoker.MethodHandle(this.object, method, flag2, flag);
         if (flag2) {
            this.getListenerList().add(this.createValue(classValue4, classValue, methodHandle, i));
         } else {
            AccessProviderUtils.AccessProvider<?>[] accessProviders = Arrays.stream(Arrays.copyOfRange(parameters, 1, parameters.length))
               .map(parameterx -> AccessProviderUtils.resolveAccessProvider(parameterx.getType(), parameterx.getName()))
               .filter(Objects::nonNull)
               .toArray(AccessProviderUtils.AccessProvider[]::new);
            this.getListenerList().add(this.createValue(classValue4, classValue, methodHandle, i, accessProviders));
         }
      }
   }

   public List<Listener<?>> getListenerList() {
      return this.listenerList;
   }

   private static class MethodHandle {
      public final boolean flag;
      public final Object object;
      public final Method method;
      public final boolean flag2;

      public Object[] values(Object object, Object... objects) {
         return ArrayUtils.addFirst(objects, object);
      }

      public MethodHandle(Object object, Method method, boolean flag, boolean flag2) {
         this.object = object;
         this.method = method;
         this.flag = flag;
         this.flag2 = flag2;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public <E> void handleObject(E object, AccessProviderUtils.AccessProvider<?>... accessProviders) {
         if (this.flag) {
            this.handleObject2(object);
         } else {
            Object[] objects = Arrays.stream(accessProviders).map(AccessProviderUtils.AccessProvider::get).toArray();
            if (!Arrays.stream(objects).anyMatch(Objects::isNull)) {
               this.handleObject2(this.values(object, objects));
            }
         }
      }

      public Object getObject() {
         return this.object;
      }

      public Method getMethod() {
         return this.method;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void handleObject2(Object... objects) {
         try {
            this.method.setAccessible(true);
            this.method.invoke(this.object, objects);
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }
   }
}
