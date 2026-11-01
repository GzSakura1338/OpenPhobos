package org.phobos.core.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.KeyValue;

public abstract class MapSetting<K, V> extends Setting<Map<K, V>> {
   public MapSetting(String text, @Nullable String text2, Map<K, V> map) {
      super(text, text2, map);
   }

   public abstract String describeObject(K var1, V var2);

   public MapSetting(String text, @Nullable String text2) {
      this(text, text2, new HashMap<>());
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonArray jsonArray = new JsonArray();

      for (Map.Entry<K, V> entry : this.getValue().entrySet()) {
         jsonArray.add(this.describeObject(entry.getKey(), entry.getValue()));
      }

      return jsonArray;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      Map<K, V> map = new HashMap<>();

      for (JsonElement jsonElement2 : jsonArray) {
         KeyValue<K, V> keyValue = this.resolveKeyValue(jsonElement2.getAsString());
         map.put(keyValue.getObject(), keyValue.getObject2());
      }

      this.setValue(map);
   }

   public MapSetting(String text) {
      this(text, "");
   }

   public abstract KeyValue<K, V> resolveKeyValue(String var1);
}
