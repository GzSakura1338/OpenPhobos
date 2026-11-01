package org.phobos.core.loader;

public class BootstrapClassDefiner {
   public Class<?> resolveClassValue(byte[] bytes) throws Exception, ClassNotFoundException {
      return Class.forName("java.lang.invoke.ConstantBootstraps");
   }
}
