package org.phobos.core.util.holder;

import java.util.UUID;

public class NamedUuid {
   public final String text;
   public final UUID uUID;
   public final String name;

   public NamedUuid(UUID uUID, String text, String text2) {
      this.uUID = uUID;
      this.name = text;
      this.text = text2;
   }

   public String getName() {
      return this.name;
   }

   public String getText() {
      return this.text;
   }

   public UUID getUUID() {
      return this.uUID;
   }
}
