package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Map;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class StringMapSetting extends Setting<Map<String, String>> {
   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return null;
   }

   public StringMapSetting(String text, String text2) {
      super(text, text2, Map.of());
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Map.Entry<String, String> entry : this.getValue().entrySet()) {
         jsonObject.addProperty(entry.getKey(), entry.getValue());
      }

      return jsonObject;
   }

   public StringMapSetting(String text) {
      this(text, "");
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
         this.getValue().put(entry.getKey(), entry.getValue().getAsString());
      }
   }
}
