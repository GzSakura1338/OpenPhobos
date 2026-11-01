package org.phobos.core.kit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import org.phobos.api.JsonSerializable;

public abstract class SlotItemMap implements JsonSerializable {
   public final Map<Integer, Item> integerMap = new HashMap<>();

   public Item resolveItem(int i) {
      return this.integerMap.get(i);
   }

   public void handleSlot(Slot slot, Item item) {
      this.integerMap.put(slot.id, item);
   }

   public Map<Integer, Item> getBlockPosMap() {
      return this.integerMap;
   }

   public Item resolveItem2(Slot slot) {
      return this.integerMap.get(slot.id);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      if (this.isEmpty()) {
         return jsonObject;
      }

      try {
         for (Map.Entry<Integer, Item> entry : this.integerMap.entrySet()) {
            jsonObject.addProperty(String.valueOf(entry.getKey()), Item.getRawId(entry.getValue()));
         }
      } catch (Throwable var4) {
      }

      return jsonObject;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      try {
         JsonObject jsonObject = jsonElement.getAsJsonObject();

         for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            Integer integer = Integer.valueOf(entry.getKey());
            Item item = Item.byRawId(entry.getValue().getAsInt());
            this.integerMap.put(integer, item);
         }
      } catch (Throwable var7) {
      }
   }

   public void handleInt(int i, Item item) {
      this.integerMap.put(i, item);
   }

   public void handleSlotItemMap(SlotItemMap slotItemMap) {
      for (Map.Entry<Integer, Item> entry : this.integerMap.entrySet()) {
         slotItemMap.handleInt(entry.getKey(), entry.getValue());
      }
   }

   public boolean isEmpty() {
      return this.integerMap.isEmpty();
   }

   public void clear() {
      this.integerMap.clear();
   }
}
