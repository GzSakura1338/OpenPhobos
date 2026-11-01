package org.phobos.api;

import java.util.Map;
import org.phobos.core.util.holder.TimestampPair;

public interface PlayerTextureCache {
   default void handleText(String text, long l) {
      TimestampPair timestampPair = new TimestampPair(l);
      this.getTextMap().put(text, timestampPair);
   }

   default boolean testText(String text) {
      TimestampPair timestampPair = this.getTextMap().get(text);
      if (timestampPair == null) {
         return false;
      } else if (timestampPair.check()) {
         this.getTextMap().remove(text);
         return false;
      } else {
         return true;
      }
   }

   Map<String, TimestampPair> getTextMap();

   void run();
}
