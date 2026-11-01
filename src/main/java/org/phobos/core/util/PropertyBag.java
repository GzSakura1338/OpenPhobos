package org.phobos.core.util;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class PropertyBag {
   public final Map<String, PropertyBag.Prop<?>> textMap = new HashMap<>();

   public <T> T resolveObject(String text, Class<T> classValue) {
      return (T)this.textMap.get(text).getObject();
   }

   public void clear() {
      this.textMap.clear();
   }

   public <T> void handleText(String text, @NotNull T object) {
      this.textMap.put(text, new PropertyBag.Prop(object));
   }

   public static class Prop<T> {
      public final T object;

      public Prop(T object) {
         this.object = object;
      }

      public T getObject() {
         return this.object;
      }
   }
}
