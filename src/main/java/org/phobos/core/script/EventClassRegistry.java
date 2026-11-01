package org.phobos.core.script;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.loader.ClassScanner;

public class EventClassRegistry {
   public final Map<String, Class<?>> textMap;
   public static final EventClassRegistry INSTANCE = new EventClassRegistry();
   public final Map<Class<?>, String> classValueMap = new ConcurrentHashMap<>();

   public void handleClassValue(Class<?> classValue, String text) {
      this.classValueMap.put(classValue, text);
      this.textMap.put(text, classValue);
   }

   @Nullable
   public String describeClassValue(Class<?> classValue) {
      return this.classValueMap.get(classValue);
   }

   public EventClassRegistry() {
      this.textMap = new ConcurrentHashMap<>();
   }

   public void run() {
      try {
         for (Class<?> classValue : ClassScanner.values("org.phobos.core.event.events")) {
            this.handleClassValue(classValue, classValue.getSimpleName());
         }
      } catch (Exception var5) {
      }
   }

   @Nullable
   public Class<?> resolveClassValue(String text) {
      return this.textMap.get(text);
   }
}
