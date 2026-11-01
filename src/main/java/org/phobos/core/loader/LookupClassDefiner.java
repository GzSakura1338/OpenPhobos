package org.phobos.core.loader;

import java.lang.invoke.MethodHandles;

public class LookupClassDefiner {
   public static final LookupClassDefiner INSTANCE = new LookupClassDefiner();

   public Class<?> resolveClassValue(byte[] bytes) throws IllegalAccessException {
      return MethodHandles.lookup().defineClass(bytes);
   }

   public static LookupClassDefiner getINSTANCE() {
      return INSTANCE;
   }
}
