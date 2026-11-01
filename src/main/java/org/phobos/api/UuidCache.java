package org.phobos.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public interface UuidCache {
   default boolean contains(String text) {
      return this.getTextMap().containsKey(text);
   }

   default UUID resolveUUID(String text) {
      return this.getTextMap().remove(text);
   }

   default boolean testText(String text, UUID uUID) {
      return this.getTextMap().entrySet().stream().anyMatch(entry -> entry.getKey().equalsIgnoreCase(text) && entry.getValue().equals(uUID));
   }

   @Nullable
   default String describeText(String text) {
      return this.getTextMap().keySet().stream().filter(text::contains).findFirst().orElse(null);
   }

   default void handleText(String text, UUID uUID) {
      this.getTextMap().remove(text);
      this.getTextMap().put(text, uUID);
   }

   default void clear() {
      this.getTextMap().clear();
   }

   Map<String, UUID> getTextMap();

   default void handleUUID(UUID uUID) {
      this.getTextMap().entrySet().removeIf(entry -> entry.getValue().equals(uUID));
   }

   default boolean testText2(String text) {
      return this.getTextMap().keySet().stream().anyMatch(text::equalsIgnoreCase);
   }

   default boolean testUUID(UUID uUID) {
      return this.getTextMap().containsValue(uUID);
   }

   default Set<String> getSet() {
      return this.getTextMap().keySet();
   }
}
