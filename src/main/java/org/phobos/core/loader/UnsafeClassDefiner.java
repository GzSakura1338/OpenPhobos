package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class UnsafeClassDefiner {
   public static final UnsafeClassDefiner INSTANCE = new UnsafeClassDefiner();

   public static UnsafeClassDefiner getINSTANCE() {
      return INSTANCE;
   }

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }
}
