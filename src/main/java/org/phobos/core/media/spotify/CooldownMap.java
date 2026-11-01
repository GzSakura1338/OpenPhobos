package org.phobos.core.media.spotify;

import java.util.HashMap;
import java.util.Map;

public class CooldownMap {
   public final Map<String, Long> textMap = new HashMap<>();
   public final long longValue;

   public long getLongValue() {
      return this.longValue;
   }

   public long computeText(String text) {
      Long longValue = this.textMap.get(text);
      return longValue == null ? -1L : longValue - this.longValue;
   }

   public void handleText(String text, long l) {
      this.textMap.put(text, l);
   }

   public CooldownMap(long l, long l2) {
      this.longValue = l - l2;
   }
}
