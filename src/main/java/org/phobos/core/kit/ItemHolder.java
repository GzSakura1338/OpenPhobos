package org.phobos.core.kit;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.JsonSerializable;

public class ItemHolder implements JsonSerializable {
   @Nullable
   public Item item;

   @Override
   public JsonElement createJsonPrimitive() {
      return new JsonPrimitive(Item.getRawId(this.item));
   }

   public boolean check() {
      return this.item != null;
   }

   public void setItem(@Nullable Item item) {
      this.item = item;
   }

   @Nullable
   public Item getItem() {
      return this.item;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.setItem(Item.byRawId(jsonElement.getAsInt()));
   }
}
