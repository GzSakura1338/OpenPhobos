package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class HiddenClassDefiner {
   public static final HiddenClassDefiner INSTANCE = new HiddenClassDefiner();

   public static HiddenClassDefiner getINSTANCE() {
      return INSTANCE;
   }

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }
}
