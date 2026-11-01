package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class DirectClassDefiner {
   public static final DirectClassDefiner INSTANCE = new DirectClassDefiner();

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }

   public static DirectClassDefiner getINSTANCE() {
      return INSTANCE;
   }
}
