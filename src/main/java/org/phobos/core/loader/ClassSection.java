package org.phobos.core.loader;

import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;

public class ClassSection {
   public final Map<String, URL> textMap;
   public final List<Class<?>> classValueList = new ArrayList<>();

   public Map<String, URL> getTextMap() {
      return this.textMap;
   }

   public ClassSection(Map<String, URL> map) {
      this.textMap = map;
   }

   public List<Class<?>> getClassValueList() {
      return this.classValueList;
   }

   public static ClassSection createClassSection() {
      return new ClassSection(new HashMap<>());
   }

   public void run() throws IllegalComponentException {
      if (this.textMap.isEmpty()) {
         throw new IllegalComponentException("Unable to load an empty section");
      }

      try (URLClassLoader uRLClassLoader = URLClassLoader.newInstance(this.textMap.values().toArray(new URL[0]))) {
         for (Map.Entry<String, URL> entry : this.textMap.entrySet()) {
            try {
               Class classNotFoundException = Class.forName(entry.getKey(), true, uRLClassLoader);
            } catch (ClassNotFoundException classNotFoundException) {
               throw new IllegalComponentException("Failed to locate class: " + Arrays.toString(classNotFoundException.getStackTrace()));
            }
         }
      } catch (Throwable throwable) {
         throw new IllegalComponentException("Failed to load: " + Arrays.toString(throwable.getStackTrace()));
      }
   }

   public ClassSection resolveClassSection(String text, URI uRI) throws IllegalComponentException {
      try {
         this.textMap.put(text, uRI.toURL());
         return this;
      } catch (Throwable throwable) {
         throw new IllegalComponentException("Failed to convert URI to URL");
      }
   }
}
