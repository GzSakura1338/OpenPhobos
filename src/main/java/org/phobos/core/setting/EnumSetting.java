package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.gui.element.EnumElement;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {
   @Override
   public boolean testString(@NotNull String... texts) {
      Enum<?>[] enumValues = (Enum<?>[])this.getObject().getDeclaringClass().getEnumConstants();

      for (Enum<?> enumValue : enumValues) {
         if (enumValue.name().equalsIgnoreCase(texts[0])) {
            this.testObject(enumValue);
            return true;
         }
      }

      return false;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      String text = jsonElement.getAsString();
      Enum<?>[] enumValues = (Enum<?>[])this.getObject().getDeclaringClass().getEnumConstants();

      for (Enum<?> enumValue : enumValues) {
         if (enumValue.name().equalsIgnoreCase(text)) {
            this.testObject(enumValue);
            break;
         }
      }
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new EnumElement<>(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return new JsonPrimitive(this.getValue().name());
   }

   public static Enum<?> resolveEnumValue(Enum<?> enumValue) {
      Enum<?>[] enumValues = (Enum<?>[])enumValue.getDeclaringClass().getEnumConstants();
      return enumValue.ordinal() - 1 < 0 ? enumValues[enumValues.length - 1] : enumValues[enumValue.ordinal() - 1];
   }

   public void run() {
      this.testObject(resolveEnumValue(this.getValue()));
   }

   public static Enum<?> resolveEnumValue2(Enum<?> enumValue) {
      Enum<?>[] enumValues = (Enum<?>[])enumValue.getDeclaringClass().getEnumConstants();
      return enumValues.length - 1 == enumValue.ordinal() ? enumValues[0] : enumValues[enumValue.ordinal() + 1];
   }

   public EnumSetting(String text, @Nullable String text2, T enumValue) {
      super(text, text2, enumValue);
   }

   public EnumSetting(String text, T enumValue) {
      this(text, "", enumValue);
   }

   public void run3() {
      this.testObject(resolveEnumValue2(this.getValue()));
   }
}
