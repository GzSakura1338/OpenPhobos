package org.phobos.core.util;

public class CastUtils {
   public static <T, S> T resolveObject(Class<T> classValue, S object) {
      return (T)object;
   }

   public static <T> T resolveObject2(Object object, Class<T> classValue) {
      return (T)object;
   }
}
