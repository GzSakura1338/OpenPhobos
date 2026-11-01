package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class LoaderClassDefiner {
   public static final LoaderClassDefiner INSTANCE = new LoaderClassDefiner();

   public static LoaderClassDefiner getINSTANCE() {
      return INSTANCE;
   }

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }
}
