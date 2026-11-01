package org.phobos.core.loader;

public class DefiningClassLoader extends ClassLoader {
   public Class<?> resolveClassValue(String text) throws ClassNotFoundException {
      return this.findClass(text);
   }

   public DefiningClassLoader(ClassLoader classLoader) {
      super(classLoader);
   }

   public Class<?> resolveClassValue2(String text, byte[] bytes) {
      return this.defineClass(text, bytes, 0, bytes.length);
   }
}
