package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.KeyValue;
import org.phobos.type.HashMapLinkedHashMapEnum;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class EnumToggleSetting<T extends Enum<T>> extends Setting<Map<T, Boolean>> {
   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (String text : jsonObject.keySet()) {
         Boolean booleanValue = jsonObject.get(text).getAsBoolean();
         T enumValue = this.resolveEnumValue(text);
         this.getValue().put(enumValue, booleanValue);
      }
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      throw new UnsupportedOperationException();
   }

   public boolean testEnumValue(T enumValue) {
      return this.getValue().getOrDefault(enumValue, false);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Map.Entry<T, Boolean> entry : this.getValue().entrySet()) {
         jsonObject.addProperty(entry.getKey().name(), entry.getValue());
      }

      return jsonObject;
   }

   public void handleEnumValue(T enumValue) {
      if (this.getValue().containsKey(enumValue)) {
         this.getValue().put(enumValue, !this.getValue().get(enumValue));
      }
   }

   @SafeVarargs
   public EnumToggleSetting(String text, Map<T, Boolean> map, T... enumValues) {
      super(
         text,
         "",
         HashMapLinkedHashMapEnum.LinkedHashMap
            .list2map(List.of(enumValues), enumValue -> KeyValue.createValue(enumValue, map.getOrDefault(enumValue, false)))
      );
   }

   @SafeVarargs
   public EnumToggleSetting(String text, T... enumValues) {
      this(text, HashMapLinkedHashMapEnum.LinkedHashMap.create(0), enumValues);
   }

   public T resolveEnumValue(String text) {
      T enumValue2 = this.getObject().keySet().stream().findFirst().orElse(null);
      if (enumValue2 == null) {
         return null;
      }

      Enum<?>[] enumValues = (Enum<?>[])enumValue2.getDeclaringClass().getEnumConstants();

      for (Enum<?> enumValue : enumValues) {
         if (enumValue.name().equalsIgnoreCase(text)) {
            return (T)enumValue;
         }
      }

      return null;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      return false;
   }
}
