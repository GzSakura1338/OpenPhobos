package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class ReflectionClassDefiner {
   public static final ReflectionClassDefiner INSTANCE = new ReflectionClassDefiner();

   public static ReflectionClassDefiner getINSTANCE() {
      return INSTANCE;
   }

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }
}
