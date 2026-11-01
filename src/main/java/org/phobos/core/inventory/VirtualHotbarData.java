package org.phobos.core.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.JsonSerializable;

public class VirtualHotbarData implements JsonSerializable {
   public final Map<Integer, Item> integerMap = new HashMap<>();

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Map.Entry<Integer, Item> entry : this.integerMap.entrySet()) {
         jsonObject.add(String.valueOf(entry.getKey()), new JsonPrimitive(Item.getRawId(entry.getValue())));
      }

      return jsonObject;
   }

   public static VirtualHotbarData resolveVirtualHotbarData(ClientPlayerEntity clientPlayerEntity) {
      DefaultedList<ItemStack> defaultedList = clientPlayerEntity.playerScreenHandler.getStacks();
      VirtualHotbarData virtualHotbarData = new VirtualHotbarData();

      for (int i = 0; i < defaultedList.size(); i++) {
         virtualHotbarData.handleInt(i, ((ItemStack)defaultedList.get(i)).getItem());
      }

      return virtualHotbarData;
   }

   public Map<Integer, Item> getIntegerMap() {
      return this.integerMap;
   }

   @Nullable
   public Item resolveItem(int i) {
      return this.integerMap.get(i);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
         int i = Integer.parseInt(entry.getKey());
         Item item = Item.byRawId(entry.getValue().getAsInt());
         this.integerMap.put(i, item);
      }
   }

   public void handleInt(int i, Item item) {
      this.integerMap.put(i, item);
   }
}
