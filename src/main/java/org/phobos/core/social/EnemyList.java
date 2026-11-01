package org.phobos.core.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import java.util.UUID;
import org.phobos.api.Named;
import org.phobos.api.UuidCache;
import org.phobos.api.JsonSerializable;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.core.config.ConfigManager;

public class EnemyList implements UuidCache, JsonSerializable, Named {
   public final Map<String, UUID> textMap = new HashMap<>();

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Map.Entry<String, UUID> entry : this.textMap.entrySet()) {
         jsonObject.addProperty(entry.getKey(), entry.getValue().toString());
      }

      return jsonObject;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      if (ConfigManager.getConfigSectionFilter().testBINDSMODULESEnum(BINDSMODULESEnum.ENEMIES)) {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         this.textMap.clear();

         for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            this.textMap.put(entry.getKey(), UUID.fromString(entry.getValue().getAsString()));
         }
      }
   }

   @Override
   public String getName() {
      return "enemies.json";
   }

   @Override
   public Map<String, UUID> getTextMap() {
      return this.textMap;
   }
}
