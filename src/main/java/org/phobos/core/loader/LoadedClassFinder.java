package org.phobos.core.loader;

public class LoadedClassFinder extends ClassLoader {
   public Class<?> resolveClassValue(String text) {
      return this.findLoadedClass(text);
   }

   public LoadedClassFinder(ClassLoader classLoader) {
      super(classLoader);
   }
}
