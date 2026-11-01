package org.phobos.core.util;

import java.util.HashMap;
import java.util.Map;
import org.phobos.api.Holder;
import org.phobos.api.NamedResolver;

public class ErrorRegistry implements NamedResolver {
   public final Map<String, Throwable> textMap = new HashMap<>();

   @Override
   public <T> T resolveObject(Holder<T> holder, String text) {
      try {
         return holder.getObject();
      } catch (Throwable throwable) {
         this.textMap.put(text, throwable);
         return null;
      }
   }

   public Map<String, Throwable> getTextMap() {
      return this.textMap;
   }
}
