package org.phobos.core.media.spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LimitedRegistry<T> {
   public final Map<String, T> textMap = new ConcurrentHashMap<>();
   public final List<String> textList = new ArrayList<>();
   public int intValue;

   public T resolveObject(String text) {
      return this.textMap.get(text);
   }

   public void handleText(String text, T object) {
      if (text != null && object != null) {
         if (this.textList.size() > this.intValue) {
            String text2 = this.textList.removeFirst();
            this.textMap.remove(text2);
         }

         this.textMap.put(text, object);
         this.textList.add(text);
      }
   }

   public LimitedRegistry(int i) {
      this.intValue = i;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public boolean testText(String text) {
      return this.textMap.containsKey(text);
   }

   public void clear() {
      this.textMap.clear();
      this.textList.clear();
   }
}
